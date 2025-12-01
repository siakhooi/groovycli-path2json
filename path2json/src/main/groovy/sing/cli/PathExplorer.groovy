package sing.cli

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes

class PathExplorer {
    MyPath traverse(path){
        Path file = Paths.get(path)
        def children = []
        def isDirectory
        def isOther
        def isRegularFile
        def isSymbolicLink
        def creationTime
        def creationTimeInMillis
        def lastAccessTime
        def lastAccessTimeInMillis
        def lastModifiedTime
        def lastModifiedTimeInMillis
        def size
        try{
            BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class)
            isDirectory=attr.isDirectory()
            isOther=attr.isOther()
            isRegularFile=attr.isRegularFile()
            isSymbolicLink=attr.isSymbolicLink()
            creationTime=attr.creationTime().toString()
            creationTimeInMillis=attr.creationTime().toMillis()
            lastAccessTime=attr.lastAccessTime().toString()
            lastAccessTimeInMillis=attr.lastAccessTime().toMillis()
            lastModifiedTime=attr.lastModifiedTime().toString()
            lastModifiedTimeInMillis=attr.lastModifiedTime().toMillis()
            size=attr.size()
            if (isDirectory) {
                def entries = Files.list(file).collect { it }
                entries.sort(new PathComparator())
                entries.each {
                    children << traverse(it.toString())
                }
            }
        }catch(Exception e){
            System.err.println("Error ${file}: ${e.message}")
        }
        return new MyPath(
            path: path,
            isDirectory: isDirectory,
            isOther: isOther,
            isRegularFile: isRegularFile,
            isSymbolicLink: isSymbolicLink,
            creationTime: creationTime,
            creationTimeInMillis: creationTimeInMillis,
            lastAccessTime: lastAccessTime,
            lastAccessTimeInMillis: lastAccessTimeInMillis,
            lastModifiedTime: lastModifiedTime,
            lastModifiedTimeInMillis: lastModifiedTimeInMillis,
            size: size,
            children: children
        )
    }
}
