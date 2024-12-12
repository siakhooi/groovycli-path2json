setup() {
    load 'common-setup'
    common_setup

    outputExpected=$BATS_TEST_FILENAME.$BATS_TEST_DESCRIPTION.expected.json
}

@test "basic" {
    tempDir=$(mktemp -d)
    cd "$tempDir"
    run path2json

    data=$output
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == true  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == false  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$tempDir"  ]
    assert [ $( jq '.children|length' <<<$data ) == 0  ]
}
@test "subdirectories" {
    tempDir=$(mktemp -d)
    cd "$tempDir"
    mkdir -p a/b/c
    echo -n '1234' > a/b/c/d
    echo -n '123' > a/b/e
    run path2json

    data=$output
    test_path=$tempDir
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == true  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == false  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 1  ]

    data=$(jq -r '.children[0]'<<< $output)
    test_path=$tempDir/a
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == true  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == false  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 1  ]

    data=$(jq -r '.children[0].children[0]'<<< $output)
    test_path=$tempDir/a/b
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == true  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == false  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 2  ]

    data=$(jq -r '.children[0].children[0].children[0]'<<< $output)
    test_path=$tempDir/a/b/e
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == false  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == true  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 0  ]

    data=$(jq -r '.children[0].children[0].children[1]'<<< $output)
    test_path=$tempDir/a/b/c
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == true  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == false  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 1  ]

    data=$(jq -r '.children[0].children[0].children[1].children[0]'<<< $output)
    test_path=$tempDir/a/b/c/d
    assert [ $( jq '.creationTime==null' <<<$data ) == false  ]
    assert [ $( jq '.creationTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastAccessTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTime==null' <<<$data ) == false  ]
    assert [ $( jq '.lastModifiedTimeInMillis==null' <<<$data ) == false  ]
    assert [ $( jq '.size==null' <<<$data ) == false  ]
    assert [ $( jq '.isDirectory' <<<$data ) == false  ]
    assert [ $( jq '.isRegularFile' <<<$data ) == true  ]
    assert [ $( jq '.isSymbolicLink' <<<$data ) == false  ]
    assert [ $( jq '.isOther' <<<$data ) == false  ]
    assert [ $( jq -r '.path' <<<$data ) == "$test_path"  ]
    assert [ $( jq '.children|length' <<<$data ) == 0  ]
}
