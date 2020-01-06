import hudson.model.*

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
        stage('Deploy') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 1',
                              'stepName': 'deploy',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainName': 'argos-test-app',
                              'runId': "${BUILD_NUMBER}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.snapshotsUrl} -DrepositoryId=nexus -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}
