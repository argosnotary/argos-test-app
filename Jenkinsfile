import hudson.model.*
import java.text.SimpleDateFormat

def dateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
def date = new Date()
def timestamp = dateFormat.format(new Date())

pipeline {
    agent any
    environment {
        VERSION = "${VERSION}"
    }
    stages {
        stage('Clean') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 1',
                              'stepName': 'clean',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainName': 'argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    mvn '-s settings.xml clean'
                }
            }
        }
        stage('Build') {
            steps {
	            argosWrapper(['layoutSegmentName': 'segment 2',
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
                argosWrapper(['layoutSegmentName': 'segment 3',
                              'stepName': 'deploy',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainName': 'argos-test-app',
                              'runId': "${timestamp}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.snapshotsUrl} -DrepositoryId=nexus -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Approve') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 4',
                              'stepName': 'approve',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainName': 'argos-test-app',
                              'runId': "${timestamp}"])
                {
                    script {
                        approve()
                    }
                }
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}

def approve() {
    sh "mkdir target/approve"
    sh "unzip target/argos-test-app*.dar -d target/approve"
    sh "cp target/argos-test-app.war target/approve/argos-test-app.war"
}

