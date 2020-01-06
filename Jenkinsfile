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
	            argosWrapper(['layoutSegmentName': 'segment 1',
	                          'stepName': 'build',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainName': 'argos-test-app',
				              'runId': "${BUILD_NUMBER}"])
	            {
	                mvn '-s settings.xml install xldeploy:generate-deployment-package'
	            }
            }
        }
        /*
        stage('Sonar') {
            steps {
	            argosWrapper(['stepName': 'sonar',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainId': 'argos-test-app'])
	            {
                	mvn "verify sonar:sonar -Dsonar.host.url=http://sonarqube:9000"
                }
            }
        }*/
    }
}

def mvn(args) {
    sh "mvn ${args}"
}
