#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  if [ ! -z "$TRAVIS_TAG" ]; then
    echo "Deploying release"
    gpg --import config/travis/private-key.gpg
    echo ${TRAVIS_TAG}
    mvn versions:set -D newVersion=${TRAVIS_TAG}
    mvn clean deploy --settings config/travis/mvn-settings.xml -Prelease -DskipTests=true
  else
    echo "Deploying snapshot"
    gpg --import config/travis/private-key.gpg
    mvn clean deploy --settings config/travis/mvn-settings.xml -B -U -Prelease -DskipTests=true
  fi
fi