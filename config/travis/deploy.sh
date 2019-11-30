#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  if [ "$TRAVIS_BRANCH" == 'master' ]; then
    echo "Deploying release"
    gpg --import config/travis/private-key.gpg
    mvn deploy --settings config/travis/mvn-settings.xml -B -U -P default,release -DskipTests=true -q -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn
  else
    echo "Deploying snapshot"
    gpg --import config/travis/private-key.gpg
    mvn deploy --settings config/travis/mvn-settings.xml -B -U -P default,release -DskipTests=true -q -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn
  fi
fi