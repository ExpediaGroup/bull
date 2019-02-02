#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  if [ ! -z "$TRAVIS_TAG" ]; then
    echo "Deploying release"
    gpg --import config/travis/private-key.gpg
    mvn versions:set -D newVersion=${TRAVIS_TAG}
    mvn clean deploy --settings config/travis/mvn-settings.xml -P release -DskipTests=true
  else
    echo "Deploying snapshot"
    echo ${TRAVIS_TAG}
    gpg --import config/travis/private-key.gpg
    mvn clean deploy --settings config/travis/mvn-settings.xml -B -U -P -DskipTests=true
  fi
fi