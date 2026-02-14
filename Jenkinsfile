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
        DEPLOY_DIR = "/home/ubuntu/ecommerce-project"
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
                    def VERSION = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()

                    env.VERSION = VERSION
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
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        /* ================= BUILD & DEPLOY ================= */

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

                        def serviceChanged = currentBuild.changeSets.any { changeSet ->
                            changeSet.items.any { item ->
                                item.affectedFiles.any { file ->
                                    file.path.startsWith("${svc.name}/")
                                }
                            }
                        }

                        if (serviceChanged) {

                            echo "Building ${svc.name}..."

                            dir("${svc.name}") {

                                sh 'mvn clean package -DskipTests'

                                sh """
                                    docker build -t ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} .
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION}

                                    docker tag ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:latest
                                """
                            }

                            echo "Deploying ${svc.name}..."

                            sh """
                                cd ${DEPLOY_DIR}
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
