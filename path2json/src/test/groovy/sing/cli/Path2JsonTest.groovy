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
        def result = app.traverse(tempDirName)

        then:
        def expected="{\"isDirectory\":true,\"path\":\"${tempDirName}\",\"children\":[]}"
        expected == JsonOutput.toJson(result)
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
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"${tempDirName}\",\"children\":[]}"
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
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"$tempDirName\",\"children\":[]}"
    }
    def "traverse subdirectories"() {
        setup:
        def app = new Path2Json()
        Path tempDir = Files.createTempDirectory("testsub_");
        def tempDirName=tempDir.toString()
        Path sub1 = Files.createTempDirectory(tempDir, "sub1_")
        Path file1 = Files.createTempFile(sub1, "sub1file_", ".txt")

        when:
        def result = app.traverse(tempDirName)

        then:
        def expectedObject=[isDirectory: true, path: tempDirName, children: [
            [isDirectory: true, path: sub1.toString(), children: [
                [isDirectory: false, path: file1.toString(), children: []]
            ]]
        ]]

        def expected=JsonOutput.toJson(expectedObject)
        expected == JsonOutput.toJson(result)
    }
}
