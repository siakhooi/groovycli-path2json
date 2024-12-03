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
        def actual=(new JsonBuilder(result)).toString()
        def expected=(new JsonBuilder(path: "/tmp")).toString()
        actual == expected
    }
    def "run() print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def app = new Path2Json()

        when:
        app.run("/tmp")

        then:
        buffer.toString() == "{\n    \"path\": \"/tmp\"\n}"
    }
    def "main print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def currentPath=System.getProperty("user.dir")

        when:
        Path2Json.main()

        then:
        buffer.toString() == "{\n    \"path\": \"$currentPath\"\n}"
    }
}
