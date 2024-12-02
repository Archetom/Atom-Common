VERSION=1.0.0

default:install

clean:
	@mvn clean

install:clean
	@mvn install -U

deploy:clean
	@mvn deploy

version:
	@mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$(VERSION)