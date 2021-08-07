pipelineJob('real-ci-front-backend-by-dsl') {
    parameters {
        choiceParam('environment', ['sit', 'uat', 'prod'], 'Build environment')
    }

    definition {
        cps {
            script('''
pipeline {
    agent any

    tools {
        nodejs "node-jenkins"
        maven "mvn-jenkins"
        jdk 'jdk8'
    }

    stages {
        stage('Git checkout') {
            steps {
                script {
                    git branch: 'master', url: 'https://github.com/samyum918/jenkins-ci-cd-test'
                }
            }
        }

        stage('Build Frontend and Backend') {
            steps {
                script {
                    sh "mkdir -pv ${params.environment}"
                    dir('jenkins-test-frontend') {
                        echo "[----- Building jenkins-test-frontend - ${params.environment} -----]"
                        sh "npm install"
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
    }
}
      '''.stripIndent())
            sandbox()
        }
    }
}