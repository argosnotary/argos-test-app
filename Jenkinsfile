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
                              'privateKeyCredentialId': 'default-npa2',
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
	            			  'privateKeyCredentialId': 'default-npa2',
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
                              'privateKeyCredentialId': 'default-npa2',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.releasesUrl} -DrepositoryId=nexus -Drevision=${revision} -Dversion=${revision} -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Collect xldeploy dar') {
            steps {
            argosWrapper(['layoutSegmentName': 'jenkins',
                              'stepName': 'collect_dar',
                              'privateKeyCredentialId': 'default-npa2',
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    script {
                        downLoadKey = sh(returnStdout: true, script: "curl -u admin:admin http://xldeploy:4516/deployit/export/deploymentpackage/Applications/argos/argos-test-app/${revision}")
                        sh "mkdir target/collect; wget --http-user admin --http-password admin http://xldeploy:4516/deployit/internal/download/${downLoadKey} -O temp.zip; unzip temp.zip; rm temp.zip"
                    }
                }
            }
        }
        stage('Deploy to tomcat') {
            steps {
                script {
                    properties([[$class: 'JiraProjectProperty'], disableConcurrentBuilds()])
                }
                xldDeploy serverCredentials: 'xldeploy-credentials', environmentId: 'Environments/argos/argos', packageId: "Applications/argos/argos-test-app/${revision}"
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}


