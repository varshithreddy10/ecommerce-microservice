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
                    echo "🔖 Generating Git commit hash version..."
                    env.VERSION = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()

                    echo "✅ Using Version: ${env.VERSION}"
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

                echo "✅ Docker login successful"
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

                    def isFirstBuild = (currentBuild.number == 47 || currentBuild.changeSets.isEmpty())
                    def changedServices = []

                    if (isFirstBuild) {
                        echo "🔥 FIRST BUILD DETECTED — Full system deployment"
                    }

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

                                echo "🐳 Building Docker image for ${svc.name}"
                                sh """
                                    docker build -t ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} .
                                    docker tag ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                """

                                echo "📤 Pushing images to DockerHub"
                                sh """
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION}
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                """
                            }
                        }
                    }

                    if (isFirstBuild) {

                        echo "🚀 Starting full microservices stack..."

                        sh """
                            mkdir -p ${DEPLOY_DIR}
                            cd ${DEPLOY_DIR}
                            docker compose down -v || true
                            docker compose pull
                            docker compose up -d
                        """

                    } else if (changedServices.size() > 0) {

                        echo "🔄 Restarting only changed services..."

                        for (name in changedServices) {
                            sh """
                                cd ${DEPLOY_DIR}
                                docker compose pull ${name}
                                docker compose up -d --no-deps --force-recreate ${name}
                            """
                        }
                    } else {
                        echo "⚡ No changes detected. Nothing to deploy."
                    }

                    echo "🧹 Cleaning unused Docker images..."
                    sh "docker image prune -af || true"
                }
            }
        }
    }

    post {
        always {
            echo "🚪 Logging out from DockerHub..."
            sh 'docker logout || true'
        }

        success {
            echo "🎉 CI/CD Pipeline Completed Successfully!"
        }

        failure {
            echo "❌ Pipeline Failed!"
        }
    }
}
