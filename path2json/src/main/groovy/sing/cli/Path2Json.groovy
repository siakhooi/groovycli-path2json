package sing.cli
import groovy.json.JsonOutput

class Path2Json {
    void printPath(myPath){
        print(JsonOutput.toJson(myPath))
    }
    void run(path) {
        def pathExplorer = new PathExplorer()
        printPath(pathExplorer.traverse(path))
    }

    static void main(String[] args) {
        def currentPath=System.getProperty("user.dir")
        (new Path2Json()).run(currentPath)
    }
}