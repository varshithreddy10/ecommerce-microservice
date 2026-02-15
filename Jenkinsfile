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

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Generate Commit Version') {
            steps {
                script {
                    env.VERSION = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()

                    echo "Using Version: ${env.VERSION}"
                }
            }
        }

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

                    def anyServiceBuilt = false

                    for (svc in services) {

                        def serviceChanged = currentBuild.changeSets.any { changeSet ->
                            changeSet.items.any { item ->
                                item.affectedFiles.any { file ->
                                    file.path.startsWith("${svc.name}/")
                                }
                            }
                        }

                        if (serviceChanged || currentBuild.number == 33) {

                            anyServiceBuilt = true
                            echo "Building ${svc.name}..."

                            dir("${svc.name}") {
                                sh 'mvn clean package -DskipTests'

                                sh """
                                    docker build -t ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION} .
                                    docker push ${DOCKERHUB_USERNAME}/${svc.image}:${env.VERSION}
                                """
                            }

                            echo "Deploying ${svc.name}..."

                            sh """
                                if [ ! -d "${DEPLOY_DIR}" ]; then
                                    echo "Deployment directory not found!"
                                    exit 1
                                fi

                                cd ${DEPLOY_DIR}

                                VERSION=${env.VERSION} docker compose pull ${svc.name}
                                VERSION=${env.VERSION} docker compose up -d --no-deps --force-recreate ${svc.name}
                            """
                        }
                    }

                    if (!anyServiceBuilt) {
                        echo "No microservice changes detected. Skipping build."
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
            echo "CI/CD completed successfully 🚀"
        }
        failure {
            echo "Pipeline failed ❌"
        }
    }
}
