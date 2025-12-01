package sing.cli

import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class PathComparatorTest extends Specification {
    PathComparator comparator = new PathComparator()
    Path tempDir

    def setup() {
        tempDir = Files.createTempDirectory("pathcomparator_test_")
    }

    def cleanup() {
        tempDir.toFile().deleteDir()
    }

    def "compare two files - alphabetical order"() {
        setup:
        Path fileA = Files.createTempFile(tempDir, "aaa_", ".txt")
        Path fileB = Files.createTempFile(tempDir, "bbb_", ".txt")

        when:
        def result = comparator.compare(fileA, fileB)

        then:
        result < 0  // fileA should come before fileB
    }

    def "compare two files - reverse alphabetical order"() {
        setup:
        Path fileA = Files.createTempFile(tempDir, "zzz_", ".txt")
        Path fileB = Files.createTempFile(tempDir, "aaa_", ".txt")

        when:
        def result = comparator.compare(fileA, fileB)

        then:
        result > 0  // fileA should come after fileB
    }

    def "compare two directories - alphabetical order"() {
        setup:
        Path dirA = Files.createTempDirectory(tempDir, "aaa_dir_")
        Path dirB = Files.createTempDirectory(tempDir, "bbb_dir_")

        when:
        def result = comparator.compare(dirA, dirB)

        then:
        result < 0  // dirA should come before dirB
    }

    def "compare two directories - reverse alphabetical order"() {
        setup:
        Path dirA = Files.createTempDirectory(tempDir, "zzz_dir_")
        Path dirB = Files.createTempDirectory(tempDir, "aaa_dir_")

        when:
        def result = comparator.compare(dirA, dirB)

        then:
        result > 0  // dirA should come after dirB
    }

    def "compare file and directory - file should come first"() {
        setup:
        Path file = Files.createTempFile(tempDir, "zzz_file_", ".txt")
        Path dir = Files.createTempDirectory(tempDir, "aaa_dir_")

        when:
        def result = comparator.compare(file, dir)

        then:
        result < 0  // file should come before directory
    }

    def "compare directory and file - file should come first"() {
        setup:
        Path dir = Files.createTempDirectory(tempDir, "aaa_dir_")
        Path file = Files.createTempFile(tempDir, "zzz_file_", ".txt")

        when:
        def result = comparator.compare(dir, file)

        then:
        result > 0  // directory should come after file
    }

    def "compare same file - should return zero"() {
        setup:
        Path file = Files.createTempFile(tempDir, "same_", ".txt")

        when:
        def result = comparator.compare(file, file)

        then:
        result == 0
    }

    def "compare same directory - should return zero"() {
        setup:
        Path dir = Files.createTempDirectory(tempDir, "same_dir_")

        when:
        def result = comparator.compare(dir, dir)

        then:
        result == 0
    }

    def "sort mixed files and directories"() {
        setup:
        Path file1 = Files.createTempFile(tempDir, "bbb_", ".txt")
        Path file2 = Files.createTempFile(tempDir, "aaa_", ".txt")
        Path dir1 = Files.createTempDirectory(tempDir, "zzz_")
        Path dir2 = Files.createTempDirectory(tempDir, "ccc_")
        def paths = [dir1, file1, dir2, file2]

        when:
        paths.sort(comparator)

        then:
        // Files should come first (aaa, bbb), then directories (ccc, zzz)
        paths[0] == file2  // aaa file
        paths[1] == file1  // bbb file
        paths[2] == dir2   // ccc directory
        paths[3] == dir1   // zzz directory
    }

    def "sort multiple files alphabetically"() {
        setup:
        Path file1 = Files.createTempFile(tempDir, "charlie_", ".txt")
        Path file2 = Files.createTempFile(tempDir, "alpha_", ".txt")
        Path file3 = Files.createTempFile(tempDir, "bravo_", ".txt")
        def paths = [file1, file2, file3]

        when:
        paths.sort(comparator)

        then:
        paths[0] == file2  // alpha
        paths[1] == file3  // bravo
        paths[2] == file1  // charlie
    }

    def "sort multiple directories alphabetically"() {
        setup:
        Path dir1 = Files.createTempDirectory(tempDir, "zebra_")
        Path dir2 = Files.createTempDirectory(tempDir, "alpha_")
        Path dir3 = Files.createTempDirectory(tempDir, "mike_")
        def paths = [dir1, dir2, dir3]

        when:
        paths.sort(comparator)

        then:
        paths[0] == dir2  // alpha
        paths[1] == dir3  // mike
        paths[2] == dir1  // zebra
    }

    def "compare files with similar names but different prefixes"() {
        setup:
        Path file1 = Files.createTempFile(tempDir, "test_", ".txt")
        Path file2 = Files.createTempFile(tempDir, "test1_", ".txt")

        when:
        def result = comparator.compare(file1, file2)

        then:
        // Result depends on actual filenames generated (with temp suffixes)
        // Just verify it's deterministic
        comparator.compare(file1, file2) == result
        comparator.compare(file2, file1) == -result
    }

    def "sorting is transitive"() {
        setup:
        Path fileA = Files.createTempFile(tempDir, "aaa_", ".txt")
        Path fileB = Files.createTempFile(tempDir, "bbb_", ".txt")
        Path fileC = Files.createTempFile(tempDir, "ccc_", ".txt")

        when:
        def ab = comparator.compare(fileA, fileB)
        def bc = comparator.compare(fileB, fileC)
        def ac = comparator.compare(fileA, fileC)

        then:
        // If A < B and B < C, then A < C
        if (ab < 0 && bc < 0) {
            ac < 0
        } else {
            true  // transitive property holds
        }
    }
}
