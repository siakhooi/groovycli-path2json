package sing.cli

import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class PathExplorerTest extends Specification {
    def pathExplorer = new PathExplorer()

    def "traverse empty directory"() {
        setup:
        Path tempDir = Files.createTempDirectory("traverse_empty_")
        def tempDirName = tempDir.toString()

        when:
        def actual = pathExplorer.traverse(tempDirName)

        then:
        def expected = new MyPath(
            path: tempDirName,
            isDirectory: true,
            isOther: false,
            isRegularFile: false,
            isSymbolicLink: false,
            children: []
        )
        AssertMyPath.comparePath(actual, expected)

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse single file"() {
        setup:
        Path tempFile = Files.createTempFile("traverse_file_", ".txt")
        def tempFileName = tempFile.toString()
        new File(tempFileName).text = "test content"

        when:
        def actual = pathExplorer.traverse(tempFileName)

        then:
        actual.path == tempFileName
        actual.isDirectory == false
        actual.isOther == false
        actual.isRegularFile == true
        actual.isSymbolicLink == false
        actual.size == 12
        actual.children.size() == 0

        cleanup:
        tempFile.toFile().delete()
    }

    def "traverse directory with single file"() {
        setup:
        Path tempDir = Files.createTempDirectory("traverse_dir_with_file_")
        def tempDirName = tempDir.toString()
        Path file1 = Files.createTempFile(tempDir, "file1_", ".txt")
        new File(file1.toString()).text = "content1"

        when:
        def actual = pathExplorer.traverse(tempDirName)

        then:
        def fileObj = new MyPath(
            path: file1,
            isDirectory: false,
            isOther: false,
            isRegularFile: true,
            isSymbolicLink: false,
            size: 8,
            children: []
        )
        def expected = new MyPath(
            path: tempDir,
            isDirectory: true,
            isOther: false,
            isRegularFile: false,
            isSymbolicLink: false,
            children: [fileObj]
        )
        AssertMyPath.comparePath(actual, expected)

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse directory with multiple files"() {
        setup:
        Path tempDir = Files.createTempDirectory("traverse_multi_files_")
        def tempDirName = tempDir.toString()
        Path file1 = Files.createTempFile(tempDir, "aaa_", ".txt")
        Path file2 = Files.createTempFile(tempDir, "bbb_", ".txt")
        new File(file1.toString()).text = "content1"
        new File(file2.toString()).text = "content22"

        when:
        def actual = pathExplorer.traverse(tempDirName)

        then:
        actual.children.size() == 2
        actual.children[0].path.toString() == file1.toString()
        actual.children[1].path.toString() == file2.toString()
        actual.children[0].size == 8
        actual.children[1].size == 9

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse subdirectories with files"() {
        setup:
        Path tempDir = Files.createTempDirectory("traversesub_")
        def tempDirName = tempDir.toString()
        Path sub1 = Files.createTempDirectory(tempDir, "sub1_")
        Path file1 = Files.createTempFile(sub1, "sub1file_", ".txt")
        new File(file1.toString()).text = "1234"

        when:
        def actual = pathExplorer.traverse(tempDirName)

        then:
        def fileObj = new MyPath(
            path: file1,
            isDirectory: false,
            isOther: false,
            isRegularFile: true,
            isSymbolicLink: false,
            size: 4,
            children: []
        )
        def subObj = new MyPath(
            path: sub1,
            isDirectory: true,
            isOther: false,
            isRegularFile: false,
            isSymbolicLink: false,
            children: [fileObj]
        )
        def expected = new MyPath(
            path: tempDir,
            isDirectory: true,
            isOther: false,
            isRegularFile: false,
            isSymbolicLink: false,
            children: [subObj]
        )
        AssertMyPath.comparePath(actual, expected)

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse nested directory structure"() {
        setup:
        Path tempDir = Files.createTempDirectory("traverse_nested_")
        Path sub1 = Files.createTempDirectory(tempDir, "sub1_")
        Path sub2 = Files.createTempDirectory(sub1, "sub2_")
        Path file1 = Files.createTempFile(sub2, "deepfile_", ".txt")
        new File(file1.toString()).text = "deep"

        when:
        def actual = pathExplorer.traverse(tempDir.toString())

        then:
        actual.children.size() == 1
        actual.children[0].children.size() == 1
        actual.children[0].children[0].children.size() == 1
        actual.children[0].children[0].children[0].size == 4

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse directory with mixed files and subdirectories"() {
        setup:
        Path tempDir = Files.createTempDirectory("traverse_mixed_")
        Path file1 = Files.createTempFile(tempDir, "aaa_", ".txt")
        Path file2 = Files.createTempFile(tempDir, "bbb_", ".txt")
        Path subDir = Files.createTempDirectory(tempDir, "zzz_sub_")
        Path subFile = Files.createTempFile(subDir, "subfile_", ".txt")
        new File(file1.toString()).text = "f1"
        new File(file2.toString()).text = "f2"
        new File(subFile.toString()).text = "sf"

        when:
        def actual = pathExplorer.traverse(tempDir.toString())

        then:
        // Files should come before directories
        actual.children.size() == 3
        actual.children[0].isRegularFile == true
        actual.children[1].isRegularFile == true
        actual.children[2].isDirectory == true
        actual.children[2].children.size() == 1

        cleanup:
        tempDir.toFile().deleteDir()
    }

    def "traverse handles nonexistent path gracefully"() {
        setup:
        def nonexistentPath = "/nonexistent/path/that/does/not/exist"

        when:
        def actual = pathExplorer.traverse(nonexistentPath)

        then:
        actual.path == nonexistentPath
        actual.isDirectory == null
        actual.isOther == null
        actual.isRegularFile == null
        actual.isSymbolicLink == null
        actual.children.size() == 0
    }

    def "traverse empty file has zero size"() {
        setup:
        Path tempFile = Files.createTempFile("empty_file_", ".txt")
        def tempFileName = tempFile.toString()

        when:
        def actual = pathExplorer.traverse(tempFileName)

        then:
        actual.size == 0
        actual.isRegularFile == true

        cleanup:
        tempFile.toFile().delete()
    }

    def "traverse verifies file attributes are populated"() {
        setup:
        Path tempFile = Files.createTempFile("attrs_test_", ".txt")
        def tempFileName = tempFile.toString()
        new File(tempFileName).text = "test"

        when:
        def actual = pathExplorer.traverse(tempFileName)

        then:
        actual.creationTime != null
        actual.creationTimeInMillis != null
        actual.creationTimeInMillis > 0
        actual.lastAccessTime != null
        actual.lastAccessTimeInMillis != null
        actual.lastAccessTimeInMillis > 0
        actual.lastModifiedTime != null
        actual.lastModifiedTimeInMillis != null
        actual.lastModifiedTimeInMillis > 0

        cleanup:
        tempFile.toFile().delete()
    }
}
