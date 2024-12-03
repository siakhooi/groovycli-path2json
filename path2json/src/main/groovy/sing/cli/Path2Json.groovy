package sing.cli
import groovy.json.JsonBuilder

class Path2Json {
    MyPath traverse(path){
        return new MyPath(path: path)
    }
    void printPath(myPath){
    print(new JsonBuilder(myPath).toPrettyString())
    }
    void run(path) {
        printPath(traverse(path))
    }

    static void main(String[] args) {
        def currentPath=System.getProperty("user.dir")
        (new Path2Json()).run(currentPath)
    }
}

class MyPath {
    def path
}