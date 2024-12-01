package sing.cli

class Path2Json {
    String getPath(path){
        return "{Path: \"$path\"}"
    }
    void run(path) {
        println(getPath(path))
    }

    static void main(String[] args) {
        def currentPath=System.getProperty("user.dir")
        (new Path2Json()).run(currentPath)
    }
}
