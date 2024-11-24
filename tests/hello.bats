setup() {
    load 'common-setup'
    common_setup

}


@test "echo" {
    run path2json
    assert_output 'Hello Groovy (and Sonar??)!!!'
}
