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
            agent any
            steps {
                checkout scm
            }
        }

        /* ================= ADMIN API ================= */

        stage('Build & Push Admin API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/adminapi/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-adminapi"
            }
            steps {
                dir('adminapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} adminapi"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= API GATEWAY ================= */

        stage('Build & Push API Gateway') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/apigateway/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-apigateway"
            }
            steps {
                dir('apigateway') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} apigateway"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= AUTH USER ================= */

        stage('Build & Push Auth User API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/authuser/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-authuserapi"
            }
            steps {
                dir('authuser') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} authuser"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= CART API ================= */

        stage('Build & Push Cart API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/cartapi/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-cartapi"
            }
            steps {
                dir('cartapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} cartapi"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= CUSTOMER API ================= */

        stage('Build & Push Customer API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/customerapi/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-customerapi"
            }
            steps {
                dir('customerapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} customerapi"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= EUREKA SERVER ================= */

        stage('Build & Push Eureka Server') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/eurekaserver/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-eurekaserver"
            }
            steps {
                dir('eurekaserver') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} eurekaserver"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= ORDER API ================= */

        stage('Build & Push Order API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/orderapi/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-orderapi"
            }
            steps {
                dir('orderapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} orderapi"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }

        /* ================= PRODUCT API ================= */

        stage('Build & Push Product API') {
            agent any
            when {
                beforeAgent true
                anyOf {
                    changeset "**/productapi/**"
                    expression { currentBuild.previousBuild == null }
                }
            }
            environment {
                IMAGE_NAME = "varshithreddy144/ecom-productapi"
            }
            steps {
                dir('productapi') {
                    sh 'mvn clean package -DskipTests'
                }

                sh "docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} productapi"

                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                        docker logout
                    """
                }
            }
        }
    }

    post {
        success {
            echo "All changed services pushed successfully 🚀"
        }
        failure {
            echo "Pipeline failed ❌"
        }
    }
}
