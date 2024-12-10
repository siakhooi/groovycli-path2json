package sing.cli

class AssertMyPath{

static void comparePath(MyPath actual, MyPath expected) {
    assert actual.path.toString() == expected.path.toString()
    assert actual.isDirectory == expected.isDirectory
    assert actual.isOther == expected.isOther
    assert actual.isRegularFile == expected.isRegularFile
    assert actual.isSymbolicLink == expected.isSymbolicLink
    assert actual.creationTime != null
    assert actual.creationTimeInMillis != null
    assert actual.lastAccessTime != null
    assert actual.lastAccessTimeInMillis != null
    assert actual.lastModifiedTime != null
    assert actual.lastModifiedTimeInMillis != null
    assert (!actual.isDirectory && actual.size == expected.size) || (actual.isDirectory && actual.size > 0)
    assert actual.children.size() == expected.children.size()
    for (int i = 0; i < actual.children.size(); i++) {
        comparePath(actual.children[i], expected.children[i])
    }
}
}