import hudson.model.*
import java.text.SimpleDateFormat

def dateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
def date = new Date()
def timestamp = dateFormat.format(new Date())
def revision = "1.0.${timestamp}"


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
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
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
	            			  'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
				              'runId': "${BUILD_NUMBER}"])
	            {
	                mvn '-s settings.xml install -Drevision=${timestamp} xldeploy:import'
	            }
            }
        }
        stage('Deploy') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 3',
                              'stepName': 'deploy',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.snapshotsUrl} -DrepositoryId=nexus -Drevision=${timestamp} -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Approval bob') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 4',
                              'stepName': 'approve',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    script {
                        sh 'echo approve'
                    }
                }
            }
        }
        stage('Approval alice') {
            steps {
                argosWrapper(['layoutSegmentName': 'segment 4',
                              'stepName': 'approve',
                              'privateKeyCredentialId': 'alice',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    script {
                        sh 'echo approve'
                    }
                }
            }
        }
        stage('Deploy to tomcat') {
            xldDeploy serverCredentials: 'xldeploy-credentials', environmentId: 'Environments/argos/argos', packageId: "argos/argos-test-app/1.0.${timestamp}"
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}


