package sing.cli
import groovy.json.JsonOutput
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes

class Path2Json {
    MyPath traverse(path){
        Path file = Paths.get(path)
        def children = []
        def isDirectory
        try{
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class)
            isDirectory=attr.isDirectory()
            if (isDirectory) {
                Files.list(file).each {
                    children << traverse(it.toString())
                }
            }
        }catch(Exception e){
            System.err.println("Error ${file}: ${e.message}")
        }
        return new MyPath(path: path, isDirectory: isDirectory, children: children)
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
    def children=[]
}