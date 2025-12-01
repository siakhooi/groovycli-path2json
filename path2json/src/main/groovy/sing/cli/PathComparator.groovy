package sing.cli

import java.nio.file.Files
import java.nio.file.Path

/**
 * Comparator for sorting Path objects.
 * Sorts files before directories, and within each category, sorts alphabetically by filename.
 */
class PathComparator implements Comparator<Path> {
    @Override
    int compare(Path a, Path b) {
        def aIsDir = Files.isDirectory(a)
        def bIsDir = Files.isDirectory(b)
        if (aIsDir == bIsDir) {
            return a.fileName.toString() <=> b.fileName.toString()
        }
        return aIsDir ? 1 : -1  // files before directories
    }
}
