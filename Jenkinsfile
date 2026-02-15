pipeline {
    agent any

    tools {
        maven 'usermaven'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    environment {
        DOCKERHUB_USERNAME = "varshithreddy144"
        DEPLOY_DIR = "/opt/ecommerce-project"
        COMPOSE_URL = "https://raw.githubusercontent.com/varshithreddy10/ecommerce-microservice/main/docker-compose.yml"
    }

    stages {

        stage('1️⃣ Checkout Code') {
            steps {
                echo "📥 Checking out source code..."
                checkout scm
            }
        }

        stage('2️⃣ Generate Version') {
            steps {
                script {
                    env.VERSION = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()

                    echo "🔖 Version: ${env.VERSION}"
                }
            }
        }

        stage('3️⃣ Docker Login') {
            steps {
                echo "🔐 Logging into DockerHub..."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        stage('4️⃣ Build & Deploy') {
            steps {
                script {

                    def services = [
                        [name: "adminapi", image: "ecom-adminapi"],
                        [name: "apigateway", image: "ecom-apigateway"],
                        [name: "authuser", image: "ecom-authuserapi"],
                        [name: "cartapi", image: "ecom-cartapi"],
                        [name: "customerapi", image: "ecom-customerapi"],
                        [name: "eurekaserver", image: "ecom-eurekaserver"],
                        [name: "orderapi", image: "ecom-orderapi"],
                        [name: "productapi", image: "ecom-productapi"]
                    ]

                    def isFirstBuild = (currentBuild.number == 50)
                    def changedServices = []

                    /* ================= FIRST BUILD CLEAN ================= */

                    if (isFirstBuild) {

                        echo "🔥 FIRST BUILD DETECTED — Full cleanup & rebuild"

                        sh """
                            echo "🧹 Cleaning EC2 Docker completely..."

                            docker compose -f ${DEPLOY_DIR}/docker-compose.yml down -v || true
                            docker rm -f \$(docker ps -aq) || true
                            docker volume rm \$(docker volume ls -q) || true
                            docker network prune -f || true
                            docker image rm -f \$(docker images -aq) || true

                            rm -rf ${DEPLOY_DIR}
                            mkdir -p ${DEPLOY_DIR}

                            echo "⬇ Downloading fresh docker-compose.yml"
                            curl -o ${DEPLOY_DIR}/docker-compose.yml ${COMPOSE_URL}
                        """
                    }

                    /* ================= BUILD SERVICES ================= */

                    for (svc in services) {

                        def serviceChanged = currentBuild.changeSets.any { changeSet ->
                            changeSet.items.any { item ->
                                item.affectedFiles.any { file ->
                                    file.path.startsWith("${svc.name}/")
                                }
                            }
                        }

                        if (serviceChanged || isFirstBuild) {

                            changedServices.add(svc.name)
                            echo "🏗 Building ${svc.name}"

                            dir("${svc.name}") {

                                sh 'mvn clean package -DskipTests'

                                sh """
                                    docker build -t ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} .
                                    docker tag ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} ${DOCKERHUB_USERNAME}/${svc.image}:latest

                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION}
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                """
                            }
                        }
                    }

                    /* ================= DEPLOY ================= */

                    if (isFirstBuild) {

                        echo "🚀 Starting FULL system"

                        sh """
                            cd ${DEPLOY_DIR}
                            docker compose up -d
                        """

                    } else if (changedServices.size() > 0) {

                        echo "🔄 Updating changed services only"

                        for (name in changedServices) {
                            sh """
                                cd ${DEPLOY_DIR}
                                docker compose pull ${name}
                                docker compose up -d --no-deps --force-recreate ${name}
                            """
                        }
                    } else {
                        echo "⚡ No changes detected"
                    }

                    echo "🧹 Cleaning unused images"
                    sh "docker image prune -af || true"
                }
            }
        }
    }

    post {
        always {
            echo "🚪 Docker logout"
            sh 'docker logout || true'
        }
        success {
            echo "🎉 CI/CD Completed Successfully!"
        }
        failure {
            echo "❌ Pipeline Failed!"
        }
    }
}
