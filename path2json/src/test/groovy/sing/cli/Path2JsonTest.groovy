package sing.cli

import groovy.json.JsonBuilder
import spock.lang.Specification

class Path2JsonTest extends Specification {
    def "traverse return json"() {
        setup:
        def app = new Path2Json()

        when:
        def result = app.traverse("/tmp")

        then:
        def expected=[isDirectory: true, path: "/tmp"]
        result.isDirectory == expected.isDirectory
        result.path == expected.path
    }
    def "run() print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def app = new Path2Json()

        when:
        app.run("/tmp")

        then:
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"/tmp\"}"
    }
    def "main print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def currentPath=System.getProperty("user.dir")

        when:
        Path2Json.main()

        then:
        buffer.toString() == "{\"isDirectory\":true,\"path\":\"$currentPath\"}"
    }
}
