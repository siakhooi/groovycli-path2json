package sing.cli

import spock.lang.Specification

class Path2JsonTest extends Specification {
    def "getPath return json"() {
        setup:
        def app = new Path2Json()

        when:
        def result = app.getPath("/tmp")

        then:
        result == "{Path: \"/tmp\"}"
    }
    def "run() print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def app = new Path2Json()

        when:
        app.run("/tmp")

        then:
         buffer.toString() == "{Path: \"/tmp\"}\n"
    }
    def "main print correctly"() {
        setup:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)
        def currentPath=System.getProperty("user.dir")

        when:
        Path2Json.main()

        then:
        buffer.toString() == "{Path: \"$currentPath\"}\n"
    }
}
