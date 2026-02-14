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
    }

    stages {

        /* ================= CHECKOUT ================= */

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        /* ================= GENERATE VERSION ================= */

        stage('Generate Commit Version') {
            steps {
                script {
                    VERSION = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()
                    echo "Using Version: ${VERSION}"
                }
            }
        }

        /* ================= DOCKER LOGIN ================= */

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    """
                }
            }
        }

        /* ================= BUILD FUNCTION ================= */

        stage('Build & Deploy Changed Services') {
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

                    for (svc in services) {

                        if (currentBuild.changeSets.any { changeSet ->
                            changeSet.items.any { item ->
                                item.affectedFiles.any { file ->
                                    file.path.startsWith("${svc.name}/")
                                }
                            }
                        }) {

                            echo "Building ${svc.name}..."

                            dir("${svc.name}") {
                                sh 'mvn clean package -DskipTests'
                                sh """
                                    docker build -t ${DOCKERHUB_USERNAME}/${svc.image}:${VERSION} .
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:${VERSION}
                                    docker tag ${DOCKERHUB_USERNAME}/${svc.image}:${VERSION} ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                """
                            }

                            echo "Deploying ${svc.name}..."

                            sh """
                                docker compose pull ${svc.name}
                                docker compose up -d ${svc.name}
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
        success {
            echo "Changed services built and deployed successfully 🚀"
        }
        failure {
            echo "Pipeline failed ❌"
        }
    }
}

