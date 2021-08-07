pipeline {
    agent any
    stages {
        stage('Build Frontend and Backend') {
            sh "mkdir -v ${params.environment}"
            dir('jenkins-test-frontend') {
                echo "[----- Building jenkins-test-frontend - ${params.environment} -----]"
                sh "npm run build:${params.environment}"
                sh "cp -rf ./build/* ${WORKSPACE}/jenkins-test-backend/src/main/resources/public"
                echo "[----- Building jenkins-test-frontend - ${params.environment} DONE -----]"
            }
            dir('jenkins-test-backend') {
                echo "[----- Building jenkins-test-backend - ${params.environment} -----]"
                sh 'mvn clean install -Dmaven.test.skip=true'
                echo "[----- Building jenkins-test-backend - ${params.environment} DONE -----]"
            }
        }
    }
}