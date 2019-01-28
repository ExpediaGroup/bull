#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
  if [ ! -z "$TRAVIS_TAG" ]; then
    echo "Deploying release"
    mvn clean deploy --settings config/travis/mvn-settings.xml -B -U -P sonatype-oss-release "$@" -DskipTests=true
  else
    echo "Deploying snapshot"
    mvn clean deploy --settings config/travis/mvn-settings.xml -B -U -P -DskipTests=true
  fi
fi