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
                argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'clean',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    mvn '-s settings.xml clean'
                }
            }
        }
        stage('Build') {
            steps {
	            argosWrapper(['layoutSegmentName': 'jenkins',
	                          'stepName': 'build',
	            			  'privateKeyCredentialId': 'bob',
	            			  'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
				              'runId': "${timestamp}"])
	            {
	                mvn '-s settings.xml install xldeploy:import'
	            }
            }
        }
        stage('Deploy') {
            steps {
                argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'deploy',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.snapshotsUrl} -DrepositoryId=nexus -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Collect dar') {
            steps {
                argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'approve',
                              'privateKeyCredentialId': 'bob',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${timestamp}"])
                {
                    script {
                        sh 'echo collect dar'
                    }
                }
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}


