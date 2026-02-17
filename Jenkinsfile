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

        /* ================= CHECKOUT ================= */

        stage('1️⃣ Checkout Code') {
            steps {
                echo "📥 Checking out source code..."
                checkout scm
            }
        }

        /* ================= VERSION ================= */

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

        /* ================= DOCKER LOGIN ================= */

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

        /* ================= BUILD & DEPLOY ================= */

        stage('4️⃣ Build & Deploy') {
            steps {
                script {

                    def services = [
                        [name: "adminapi",      image: "ecom-adminapi"],
                        [name: "apigateway",    image: "ecom-apigateway"],
                        [name: "authuser",      image: "ecom-authuserapi"],
                        [name: "cartapi",       image: "ecom-cartapi"],
                        [name: "customerapi",   image: "ecom-customerapi"],
                        [name: "eurekaserver",  image: "ecom-eurekaserver"],
                        [name: "orderapi",      image: "ecom-orderapi"],
                        [name: "productapi",    image: "ecom-productapi"]
                    ]

                    def isFirstBuild = (currentBuild.number == 56)
                    def changedServices = []

                    /* ===== FIRST BUILD CLEANUP ===== */

                    if (isFirstBuild) {

                        echo "🔥 FIRST BUILD DETECTED — Full Clean & Rebuild"

                        sh """
                            echo "🧹 Cleaning Docker safely..."

                            mkdir -p ${DEPLOY_DIR}
                            cd ${DEPLOY_DIR}

                            docker compose down -v || true
                            docker system prune -af --volumes || true

                            echo "🗑 Removing old docker-compose.yml"
                            rm -f docker-compose.yml || true

                            echo "⬇ Downloading fresh docker-compose.yml"
                            curl -f -o docker-compose.yml ${COMPOSE_URL}
                        """
                    }

                    /* ===== BUILD SERVICES ===== */

                    for (svc in services) {

                        def serviceChanged = currentBuild.changeSets.any { changeSet ->
                            changeSet.items.any { item ->
                                item.affectedFiles.any { file ->
                                    file.path.startsWith("${svc.name}/")
                                }
                            }
                        }

                        if (serviceChanged || isFirstBuild) {

                            echo "🏗 Building ${svc.name}..."
                            changedServices.add(svc.name)

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

                    /* ===== DEPLOY ===== */

                    if (isFirstBuild) {

                        echo "🚀 Starting FULL system (latest tags only)"

                        sh """
                            cd ${DEPLOY_DIR}
                            docker compose pull
                            docker compose up -d
                        """

                    } else if (changedServices.size() > 0) {

                        echo "🚀 Updating changed services only"

                        for (name in changedServices) {
                            sh """
                                cd ${DEPLOY_DIR}
                                docker compose pull ${name}
                                docker compose up -d --no-deps --force-recreate ${name}
                            """
                        }

                    } else {
                        echo "⚡ No microservice changes detected."
                    }
                }
            }
        }
    }

    /* ================= POST ================= */

    post {
        always {
            echo "🚪 Docker logout"
            sh 'docker logout || true'
        }
        success {
            echo "✅ CI/CD Completed Successfully"
        }
        failure {
            echo "❌ Pipeline Failed"
        }
    }
}
