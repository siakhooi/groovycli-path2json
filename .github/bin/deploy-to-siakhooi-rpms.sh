#!/bin/bash
set -e

PATH_TO_FILE=$(ls ./*.rpm)
RPM_PACKAGE_SOURCE_PATH=$(realpath "$PATH_TO_FILE")
RPM_PACKAGE_FILE=$(basename "$PATH_TO_FILE")

TMPDIR=$(mktemp -d)

readonly GIT_REPO_NAME=groovycli-path2json
readonly TARGETPATH=docs
readonly TARGETURL=https://${PUBLISH_TO_GITHUB_REPO_TOKEN}@github.com/siakhooi/rpms.git
readonly TARGETBRANCH=main
readonly TARGETDIR=rpms
readonly TARGET_GIT_EMAIL=$GIT_REPO_NAME@siakhooi.github.io
readonly TARGET_GIT_USERNAME=$GIT_REPO_NAME
TARGET_COMMIT_MESSAGE="$GIT_REPO_NAME: Auto deploy [$(date)]"

if [[ -z $PUBLISH_TO_GITHUB_REPO_TOKEN ]]; then
  echo "Error: PUBLISH_TO_GITHUB_REPO_TOKEN can not be null." >&2
  exit 1
fi

(
  cd "$TMPDIR"
  git config --global user.email "$TARGET_GIT_EMAIL"
  git config --global user.name "$TARGET_GIT_USERNAME"

  git clone -n --depth=1 -b "$TARGETBRANCH" "$TARGETURL" "$TARGETDIR"
  cd "$TARGETDIR"
  git remote set-url origin "$TARGETURL"
  git restore --staged .
  mkdir -p $TARGETPATH
  cp -v "$RPM_PACKAGE_SOURCE_PATH" "$TARGETPATH/$RPM_PACKAGE_FILE"
  git add $TARGETPATH/"$RPM_PACKAGE_FILE"
  git status
  git commit -m "$TARGET_COMMIT_MESSAGE"
  git push
)
find .
