#!/bin/bash
set -e

# shellcheck disable=SC1091
. ./release.env

sed -i 'path2json/gradle.properties'  -e 's@version = .*@version = '"$RELEASE_VERSION"'@g'
sed -i 'path2json/build.gradle'  -e 's@path2json-.*-all.jar@path2json-'"$RELEASE_VERSION"'-all.jar@g'
sed -i 'path2json/src/scripts/path2json'  -e 's@readonly VERSION=.*@readonly VERSION='"$RELEASE_VERSION"'@g'
