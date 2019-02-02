#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  if [ ! -z "$TRAVIS_TAG" ]; then
    echo "Deploying release"
    gpg --import config/travis/private-key.gpg
#    mvn versions:set -DnewVersion=${TRAVIS_TAG}
    mvn clean deploy --settings mvn-settings.xml -P release -DskipTests=true
#    mvn clean deploy --settings mvn-settings.xml -B -U -P sonatype-oss-release "$@" -DskipTests=true
  else
    echo "Deploying snapshot"
    gpg --import config/travis/private-key.gpg
    mvn clean deploy --settings mvn-settings.xml -P release -B -U -P -DskipTests=true
  fi
fi