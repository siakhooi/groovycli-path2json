setup() {
    load 'common-setup'
    common_setup

    outputExpected=$BATS_TEST_FILENAME.$BATS_TEST_DESCRIPTION.expected

}

@test "basic" {
    cd /tmp
    run path2json
    assert_output - < $outputExpected
}
