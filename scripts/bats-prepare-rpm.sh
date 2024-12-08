#!/bin/bash
set -e

rpm_file=$(ls siakhooi-path2json-*.rpm)
if [[ ! -f "$rpm_file" ]]; then
  echo "No rpm file found"
  exit 1
fi
yum install -y bats git
yum install -y ./"$rpm_file"

if [[ ! -d test_helper/bats-support ]]; then
  git clone --separate-git-dir="$(mktemp -u)" https://github.com/bats-core/bats-support.git test_helper/bats-support && rm test_helper/bats-support/.git
fi
if [[ ! -d test_helper/bats-assert ]]; then
  git clone --separate-git-dir="$(mktemp -u)" https://github.com/bats-core/bats-assert.git test_helper/bats-assert && rm test_helper/bats-assert/.git
fi
