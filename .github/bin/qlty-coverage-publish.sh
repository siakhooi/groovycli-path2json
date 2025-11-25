#!/bin/bash

set -ex
curl https://qlty.sh | bash

export PATH="$HOME/.qlty/bin:$PATH"

qlty coverage publish --add-prefix path2json/src/main/groovy path2json/build/reports/jacoco/test/jacocoTestReport.xml
