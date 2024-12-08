package sing.cli

import groovy.json.JsonOutput
import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class Path2JsonTest extends Specification {
    def "traverse return json"() {
        setup:
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("test1_");
        def tempDirName=tempDir.toString()

        when:
        def actual = app.traverse(tempDirName)

        then:
        def expectedFormat='{"isDirectory":true,"isRegularFile":false,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[]}'
        def expected=String.format(expectedFormat, "${tempDirName}")
        expected == JsonOutput.toJson(actual)
    }
    def "run() print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("test2_")
        def tempDirName=tempDir.toString()

        when:
        app.run(tempDirName)

        then:
        def actual=buffer.toString()
        def expectedFormat='{"isDirectory":true,"isRegularFile":false,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[]}'
        def expected=String.format(expectedFormat, "${tempDirName}")
        expected == actual
    }
    def "main print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def currentPath=System.getProperty("user.dir")
        Path tempDir = Files.createTempDirectory("test3_")
        def tempDirName=tempDir.toString()
        System.setProperty("user.dir", tempDirName)

        when:
        Path2Json.main()

        then:
        def actual=buffer.toString()
        def expectedFormat='{"isDirectory":true,"isRegularFile":false,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[]}'
        def expected=String.format(expectedFormat, "${tempDirName}")
        expected == actual
    }
    def "traverse subdirectories"() {
        setup:
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("testsub_");
        def tempDirName=tempDir.toString()
        Path sub1 = Files.createTempDirectory(tempDir, "sub1_")
        Path file1 = Files.createTempFile(sub1, "sub1file_", ".txt")

        when:
        def actual = app.traverse(tempDirName)

        then:

        def expectedFormat='{"isDirectory":true,"isRegularFile":false,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[{"isDirectory":true,"isRegularFile":false,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[{"isDirectory":false,"isRegularFile":true,"path":"%s","isSymbolicLink":false,"isOther":false,"children":[]}]}]}'
        def expected=String.format(expectedFormat, "${tempDirName}", "${sub1}", "${file1}")
        expected == JsonOutput.toJson(actual)
    }
}
