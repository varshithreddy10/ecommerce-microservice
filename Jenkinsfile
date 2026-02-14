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

        /* ================= DOCKER LOGIN (ONCE) ================= */

        stage('Docker Hub Login') {
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

        /* ================= ADMIN API ================= */

        stage('Build & Push Admin API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/adminapi/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-adminapi"
            }
            steps {
                dir('adminapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} adminapi
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= API GATEWAY ================= */

        stage('Build & Push API Gateway') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/apigateway/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-apigateway"
            }
            steps {
                dir('apigateway') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} apigateway
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= AUTH USER ================= */

        stage('Build & Push Auth User API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/authuser/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-authuserapi"
            }
            steps {
                dir('authuser') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} authuser
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= CART API ================= */

        stage('Build & Push Cart API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/cartapi/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-cartapi"
            }
            steps {
                dir('cartapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} cartapi
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= CUSTOMER API ================= */

        stage('Build & Push Customer API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/customerapi/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-customerapi"
            }
            steps {
                dir('customerapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} customerapi
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= EUREKA SERVER ================= */

        stage('Build & Push Eureka Server') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/eurekaserver/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-eurekaserver"
            }
            steps {
                dir('eurekaserver') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} eurekaserver
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= ORDER API ================= */

        stage('Build & Push Order API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/orderapi/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-orderapi"
            }
            steps {
                dir('orderapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} orderapi
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }

        /* ================= PRODUCT API ================= */

        stage('Build & Push Product API') {
            when {
                beforeAgent true
                anyOf {
                    changeset "**/productapi/**"
                    expression { currentBuild.number == 1 }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-productapi"
            }
            steps {
                dir('productapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh """
                    docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} productapi
                    docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                """
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
        success {
            echo "All changed services pushed successfully 🚀"
        }
        failure {
            echo "Pipeline failed ❌"
        }
    }
}
