import hudson.model.*

def VERSION = "1.0-SNAPSHOT"
def PROJECT_NAME="petclinic"

pipeline {
    agent any
    environment {
        VERSION = "${VERSION}"
    }
    stages {
        stage('Clean') {
            steps {
            	mvn 'clean'
            }
        }
        stage('Build') {
            steps {
	            argosWrapper(['stepName': 'build',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainId': 'argos-test-app'])
	            {
	                mvn '-s settings.xml install xldeploy:generate-deployment-package'
	            }
            }
        }
        stage('Sonar') {
            steps {
	            argosWrapper(['stepName': 'sonar',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainId': 'argos-test-app'])
	            {
                	mvn "verify sonar:sonar -Dsonar.projectKey=rabobank_argos-test-app -Dsonar.organization=rabobank -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=${ARGOS_TEST_SONAR_LOGIN}"
                }
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}
