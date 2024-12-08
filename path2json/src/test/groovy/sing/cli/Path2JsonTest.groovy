package sing.cli

import groovy.json.JsonBuilder
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
        def expected=[isDirectory: true, path: tempDirName]
        result.isDirectory == expected.isDirectory
        result.path == expected.path
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
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"${tempDirName}\"}"
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
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"$tempDirName\"}"
    }
}
