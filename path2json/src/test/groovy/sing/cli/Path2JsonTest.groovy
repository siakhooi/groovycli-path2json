package sing.cli

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path
import sing.cli.MyPath
import sing.cli.AssertMyPath

class Path2JsonTest extends Specification {
    def "main print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def currentPath=System.getProperty("user.dir")
        Path tempDir = Files.createTempDirectory("main_")
        def tempDirName=tempDir.toString()
        System.setProperty("user.dir", tempDirName)

        when:
        Path2Json.main()

        then:
        def actual=buffer.toString()
        def jsonSlurper = new JsonSlurper()
        def actualJson = jsonSlurper.parseText(actual)

        actualJson.path == tempDirName
        actualJson.isDirectory == true
        actualJson.isOther == false
        actualJson.isRegularFile == false
        actualJson.isSymbolicLink == false
        actualJson.creationTime != null
        actualJson.creationTimeInMillis != null
        actualJson.lastAccessTime != null
        actualJson.lastAccessTimeInMillis != null
        actualJson.lastModifiedTime != null
        actualJson.lastModifiedTimeInMillis != null
        actualJson.children[].size() == 0
    }

    def "run() print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("run_")
        def tempDirName=tempDir.toString()

        when:
        app.run(tempDirName)

        then:
        def actual=buffer.toString()
        def jsonSlurper = new JsonSlurper()
        def actualJson = jsonSlurper.parseText(actual)

        actualJson.path == tempDirName
        actualJson.isDirectory == true
        actualJson.isOther == false
        actualJson.isRegularFile == false
        actualJson.isSymbolicLink == false
        actualJson.creationTime != null
        actualJson.creationTimeInMillis != null
        actualJson.lastAccessTime != null
        actualJson.lastAccessTimeInMillis != null
        actualJson.lastModifiedTime != null
        actualJson.lastModifiedTimeInMillis != null
        actualJson.children[].size() == 0
    }

}
