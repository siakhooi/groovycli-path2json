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

    def "traverse return json"() {
        setup:
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("traverse_");
        def tempDirName=tempDir.toString()

        when:
        def actual = app.traverse(tempDirName)

        then:
        def expected = new MyPath(path: tempDirName, isDirectory: true, isOther: false,
        isRegularFile: false, isSymbolicLink: false, children: [])
        AssertMyPath.comparePath(actual, expected)
    }

    def "traverse subdirectories"() {
        setup:
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("traversesub_");
        def tempDirName=tempDir.toString()
        Path sub1 = Files.createTempDirectory(tempDir, "sub1_")
        Path file1 = Files.createTempFile(sub1, "sub1file_", ".txt")
        new File(file1.toString()).text = "1234"

        when:
        def actual = app.traverse(tempDirName)

        then:
        def fileobj=new MyPath(path: file1, isDirectory: false, isOther: false, isRegularFile: true, isSymbolicLink: false, size: 4, children: [])
        def subobj =new MyPath(path: sub1,  isDirectory: true, isOther: false, isRegularFile: false, isSymbolicLink: false,children: [fileobj])
        def expected = new MyPath(path: tempDir, isDirectory: true, isOther: false, isRegularFile: false, isSymbolicLink: false,
            children: [subobj])
        AssertMyPath.comparePath(actual, expected)
    }
}
