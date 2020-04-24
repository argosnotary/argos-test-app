@Grab('io.github.http-builder-ng:http-builder-ng-core:1.0.4')

import groovyx.net.http.HttpBuilder
import groovyx.net.http.optional.Download
import groovy.xml.XmlSlurper

def readFileString(String filePath) {
    File file = new File(filePath)
    String fileContent = file.text
    return fileContent
}

def downloadArtifact(url, filename){
    HttpBuilder.configure {
        request.uri = url
    }.get {
        Download.toFile(delegate, new File(downloadedArtifactsDir,filename))
    }
}

def downloadArtifacts() {
    new File(downloadedArtifactsDir).mkdirs()
    def xml = new XmlSlurper().parseText(readFileString(deployitManifestXmlLocation))
    xml.depthFirst().findAll { it.name() == 'fileUri' && it.text().startsWith('http') }*.parent().each {
        downloadArtifact(it.fileUri.text(),it.targetFileName.text())
    }
}

downloadArtifacts()