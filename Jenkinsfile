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
            	withDockerContainer(image: DOCKER_REGISTRY + '/' + MAVEN_IMAGE, args: '-l io.rancher.container.network=true') {
                    mvn 'clean'
                }
            }
        }
        stage('Build') {
            steps {
	            in_toto_wrap(['stepName': 'build',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainId': 'Supplychains/domain1/app1/petclinic'])
	            {
	                mvn 'install deploy xldeploy:generate-deployment-package'
	            }
            }
        }
        stage('Sonar') {
            steps {
	            in_toto_wrap(['stepName': 'sonar',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainId': 'Supplychains/domain1/app1/petclinic'])
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
