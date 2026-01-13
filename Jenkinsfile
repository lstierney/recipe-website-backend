pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    options {
        skipDefaultCheckout()
        disableConcurrentBuilds()
    }

    environment {
        // this is only for the build - not used in app
        JWT_SECRET = 'thisisthejwtsecret'
    }

    stages {
        stage('Checkout') {
            steps {
                git(
                    branch: 'main',
                    credentialsId: 'jenkins',
                    url: 'https://github.com/lstierney/recipe-website-backend/'
                )
            }
        }

        stage('Build') {
            steps {
                script {
                    withMaven(maven: 'M3') {
                        sh 'mvn clean verify site'
                    }
                }
            }
        }
    }
}
