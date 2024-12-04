verify: clean build deb bats-prepare-deb bats-run

set-version:
	scripts/set-version.sh
git-commit-and-push:
	scripts/git-commit-and-push.sh
create-release:
	scripts/create-release.sh
deb:
	./scripts/build-deb.sh
	dpkg-deb -c $$(ls path2json_*_amd64.deb)
rpm:
	rpm --eval '%{_arch}'
	./scripts/build-rpm.sh
	rpm -ql $$(ls path2json-*.rpm)

install-deb:
	apt install -y ./$$(ls path2json_*_amd64.deb)
uninstall-deb:
	apt remove path2json -y
install-rpm:
	yum install -y $$(ls path2json-*.rpm)
uninstall-rpm:
	yum remove path2json -y

bats-prepare-deb:
	scripts/bats-prepare-deb.sh
bats-prepare-rpm:
	scripts/bats-prepare-rpm.sh
bats-run:
	scripts/bats-run.sh

clean:
	 ./gradlew clean
	 rm -rf *.deb *.sha256sum *.sha512sum *.rpm bats-test-result-*.log .gradle build path2json/bin
build:
	./gradlew build
sonar:
	./gradlew sonar
run:
	./gradlew run
runjar:
	java -jar app/build/libs/app-all.jar

upgrade-gradlew:
	./gradlew wrapper --gradle-version latest

.PHONY: build
