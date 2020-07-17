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
        stage('Build') {
            steps {
	            argosWrapper(['layoutSegmentName': 'jenkins',
	                          'stepName': 'build',
	            			  'privateKeyCredentialId': 'default-sa2',
	            			  'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
				              'runId': "${GIT_COMMIT}"])
	            {
	                mvn "-s settings.xml clean install -Drevision=${revision}"
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
    }
}

def mvn(args) {
    sh "mvn ${args}"
}

