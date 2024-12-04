package sing.cli
import groovy.json.JsonOutput
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

class Path2Json {
    MyPath traverse(path){
        Path file = Paths.get(path)
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class)
        return new MyPath(path: path, isDirectory: attr.isDirectory())
    }
    void printPath(myPath){
        print(JsonOutput.toJson(myPath))
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
    def isDirectory
}