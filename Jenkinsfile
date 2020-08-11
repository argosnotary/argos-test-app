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
                              'supplyChainIdentifier': 'root_label.child_label:argos-test-app',
                              'runId': "${GIT_COMMIT}"])
                {
                    mvn "-s settings.xml deploy:deploy-file -Durl=${env.releasesUrl} -DrepositoryId=nexus -Drevision=${revision} -Dversion=${revision} -Dfile=target/argos-test-app.war -DpomFile=pom.xml"
                }
            }
        }
        stage('Release') {
            steps {
                argosRelease('argosSettingsFile': "${WORKSPACE}/argos-settings.json",
                             'releaseConfigMap': ["local-collector": 
                                [ "path": "${WORKSPACE}/target/argos-test-app.war",
                                  "basePath": "${WORKSPACE}"
                                ]
                             ])

            }
        }
        stage('Check') {
            steps {
                script {
                hash = sh(returnStdout: true, script: "sha256sum ${WORKSPACE}/target/argos-test-app.war | cut -d ' ' -f1").trim()
                result = sh(returnStdout: true, script: "curl -s ${env.argosServiceUrl}/api/supplychain/verification?artifactHashes=${hash} | cut -d':' -f2 | cut -d'}' -f1")
                if (!result == true ) {
                    currentBuild.result = 'FAILURE'
                    exit 8 
                }

                }
            }
        }
    }
}

def mvn(args) {
    sh "mvn ${args}"
}

