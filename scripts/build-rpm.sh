#!/bin/bash
set -e

./gradlew buildAppRpm

mv -v ./path2json/build/distributions/path2json-*.rpm .

rpm_file=$(ls path2json-*.rpm)

sha256sum "$rpm_file" >"$rpm_file.sha256sum"
sha512sum "$rpm_file" >"$rpm_file.sha512sum"

rpm -qipl "$rpm_file"
