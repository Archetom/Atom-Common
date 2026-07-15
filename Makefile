MVN ?= mvn
VERSION ?= 1.0.1

default:install

clean:
	@$(MVN) clean

install:clean
	@$(MVN) install -U -Dgpg.skip=true

deploy:clean
	@$(MVN) deploy

version:
	@$(MVN) versions:set -DgenerateBackupPoms=false -DnewVersion=$(VERSION)
