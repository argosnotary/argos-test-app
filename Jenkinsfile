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
                argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'clean',
                              'privateKeyCredentialId': 'default-sa2',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    mvn '-s settings.xml clean'
                }
            }
        }
        stage('Build') {
            steps {
	            argosWrapper(['layoutSegmentName': 'jenkins',
	                          'stepName': 'build',
	            			  'privateKeyCredentialId': 'default-sa2',
	            			  'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
				              'runId': "${GIT_COMMIT}"])
	            {
	                mvn "-s settings.xml install -Drevision=${revision} xldeploy:import"
	            }
            }
        }
        stage('Deploy') {
            steps {
                argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'deploy',
                              'privateKeyCredentialId': 'default-sa2',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.releasesUrl} -DrepositoryId=nexus -Drevision=${revision} -Dversion=${revision} -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Trigger Argos collect dar on xldeploy') {
            steps {
            	script {
            	    sh "xldcli -host xldeploy -username admin -password admin -f ${WORKSPACE}/xld-collect/collect.py ${revision}"
                }
            }
        }
        stage('Deploy to tomcat') {
            steps {
                xldDeploy serverCredentials: 'xldeploy-credentials', environmentId: 'Environments/argos/argos', packageId: "Applications/argos/argos-test-app/${revision}"
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}

