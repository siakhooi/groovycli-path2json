setup() {
    load 'common-setup'
    common_setup

    outputExpected=$BATS_TEST_FILENAME.$BATS_TEST_DESCRIPTION.expected.json
}

@test "basic" {
    tempDir=$(mktemp -d)
    cd "$tempDir"
    run path2json

    sed "s:TEMPDIR:${tempDir}:" "$outputExpected" | assert_output
}
@test "subdirectories" {
    tempDir=$(mktemp -d)
    cd "$tempDir"
    mkdir -p a/b/c
    touch a/b/c/d
    touch a/b/e
    run path2json

    sed "s:TEMPDIR:${tempDir}:g" "$outputExpected" | assert_output
}
