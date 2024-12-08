#!/bin/bash
set -e

deb_file=$(ls siakhooi-path2json_*_amd64.deb)
if [[ ! -f "$deb_file" ]]; then
  echo "No deb file found"
  exit 1
fi

apt install -y bats
apt install -y ./"$deb_file"

if [[ ! -d test_helper/bats-support ]]; then
  git clone --separate-git-dir="$(mktemp -u)" https://github.com/bats-core/bats-support.git test_helper/bats-support && rm test_helper/bats-support/.git
fi
if [[ ! -d test_helper/bats-assert ]]; then
  git clone --separate-git-dir="$(mktemp -u)" https://github.com/bats-core/bats-assert.git test_helper/bats-assert && rm test_helper/bats-assert/.git
fi
