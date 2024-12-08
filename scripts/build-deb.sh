#!/bin/bash
set -e

./gradlew buildAppDeb

mv -v ./path2json/build/distributions/siakhooi-path2json_*_amd64.deb .

deb_file=$(ls siakhooi-path2json_*_amd64.deb)

sha256sum "$deb_file" >"$deb_file.sha256sum"
sha512sum "$deb_file" >"$deb_file.sha512sum"
