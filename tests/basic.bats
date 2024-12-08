setup() {
    load 'common-setup'
    common_setup

    outputExpected=$BATS_TEST_FILENAME.$BATS_TEST_DESCRIPTION.expected

}

@test "basic" {
    tempDir=$(mktemp -d)
    cd "$tempDir"
    run path2json

    cat $outputExpected |sed "s:TEMPDIR:${tempDir}:" | assert_output
}
