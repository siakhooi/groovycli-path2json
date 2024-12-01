setup() {
    load 'common-setup'
    common_setup

}


@test "basic" {
    cd /tmp
    run path2json
    assert_output '{Path: "/tmp"}'
}
