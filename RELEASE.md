# Release process

## Explanation of the process sequence

The assumption that the current state of *master* will constitute the release...

#### 1. Pre-release checks

Make the following checks before performing a release:
   * Do all unit tests pass?
   * Do all examples work?
   * Does documentation build?
   * Have the new features been applied on a separate branch with compatibility with `jdk11`?

#### 2. Update VERSION and CHANGELOG

#### Increment the version number.

The application is released with the compatibility for both: `jdk15` and `jdk11`, the latest version number are:
in the [CHANGELOG.md](CHANGELOG.md) file for `jdk15` and [CHANGELOG-JDK11.md](CHANGELOG-JDK11.md) file for `jdk11`. 

The version number structure is: *major* **.** *minor* **.** *revision*.
   * *major* = significant incompatible change (e.g. partial or whole rewrite).
   * *minor* = some new functionality or changes that are mostly/wholly backward compatible.
   * *revision* = very minor changes, e.g. bugfixes.
   
For `jdk11` only, the version number is the same as the `jdk15` one, with the suffix: `-jdk11`.

e.g. if the new release version would be `X.Y.Z` then the `jdk11` correspondent one is: `X.Y.Z-jdk11`

#### Update the change log

The changes, that are going to be released, need to be specified in the `CHANGELOG` file.
Ensure it mentions any noteworthy changes since the previous release.

Make the following check before performing a release:
* Add the version is going to be released
* Place the release date next to the version number (the format is: `yyyy.mm.dd`)
* Specify under the `### Added` label everything that has been introduced by the new version 
* Specify under the `### Changed` label everything that has been changed in the new version 
* Specify under the `### Removed` label everything that has been removed in the new version 

the same changes must be reported in [CHANGELOG-JDK11.md](CHANGELOG-JDK11.md)

#### 3. Prepare the jdk11 release

All the changes implemented need to be reported to a `jdk11` compatible version.

The BULL code for the `jdk11` is slightly different so all the changes need to be reported on the other version starting
from it's latest release tag.

The first thing to do is to create a branch (that would have the same name as the `jdk15` one plus the suffix: `-jdk11`)
starting from the latest `jdk11` release tag:

```shell script
$ git checkout -b [branch name]-jdk11 [latest jdk11 release tag] 
```

e.g. if the latest `jdk11` release tag is: `1.7.0-jdk11` and the new feature branch is: `feature/my-new-feature`
the command to perform is: 

```shell script
$ git checkout -b feature/my-new-feature-jdk11 1.7.0-jdk11 
```

**IMPORTANT:** In the new branch, apply only the changes introduced comparing the code with the `jdk15` branch.
When completed, commit your code and verify that the [Travis build](https://travis-ci.org/ExpediaGroup/bull/builds) is green. 

## Example of a release process sequence

The following examples assume that your local repository is:

* a clone and the working copy is currently at the head of the master branch
* is all synced with GitHub
* the Pull Request has been approved and merged on master

The guide explains how to do a release both the `jdk15` and `jdk11` compatible:

* [JDK11 Release](https://github.com/ExpediaGroup/bull/blob/master/RELEASE.md#jdk11-release)
* [JDK15 Release](https://github.com/ExpediaGroup/bull/blob/master/RELEASE.md#jdk15-release)

**IMPORTANT:** In case something goes wrong, do not leave ghost tags or tags not related to a successful release.

### JDK11 Release

The following steps will do a release "`X.Y.Z-jdk11`"

```shell script
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean
```

#### 1. Create a branch from the latest JDK11 release tag

Assuming that:

* The latest release for `jdk11` was: `A.B.C-jdk11` 
* your new feature branch is: `feature/my-new-feature`
* The new release version is: `X.Y.Z-jdk11`

the release branch would be: `release/my-new-feature-jdk11`

```shell script
$ git checkout -b release/my-new-feature-jdk11 A.B.C-jdk11 
$ git push --set-upstream origin release/my-new-feature-jdk11 
```

#### 2. Apply the changes to the new branch

Apply all the changes you implemented to this branch.

#### 3. Change the maven version

The maven version is now set with the latest released, but you need to change it with the new one you are going to release:

```shell script
$ mvn versions:set -D newVersion=X.Y.Z-jdk11
```

Commit all the changes and verify that the [Travis build](https://travis-ci.org/ExpediaGroup/bull/builds) is green.

#### 4. Create a new tag for the release version

Once you will have created the tag, and pushed it to the remote repo, an automatic release will be performed by Travis.

```shell script
$ git tag -a X.Y.Z-jdk11 -m "my version X.Y.Z-jdk11"
$ git push origin --tags
```

#### 5. Remove the no longer needed branch

If the release went successfully, you can now delete the branch:

```shell script
$ git branch -D release/my-new-feature-jdk11
$ git push <remote_name> --delete release/my-new-feature-jdk11
```

### JDK11 Release

The following steps will do a release "`X.Y.Z`"

```shell script
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean
```

#### 1. Create a branch from the master branch

Assuming that:

* The latest release for `jdk15` was: `A.B.C` 
* your new feature branch is: `feature/my-new-feature`
* The new release version is: `X.Y.Z`

the release branch would be: `release/my-new-feature`

```shell script
$ git checkout -b release/my-new-feature
```

#### 2. Change the maven version

The maven version is now set with the latest released, but you need to change it with the new one you are going to release:

```shell script
$ mvn versions:set -D newVersion=X.Y.Z
```

Commit all the changes and verify that the [Travis build](https://travis-ci.org/ExpediaGroup/bull/builds) is green.

#### 3. Create a new tag for the release version

Once you will have created the tag, and pushed it to the remote repo, an automatic release will be performed by Travis.

```shell script
$ git tag -a X.Y.Z -m "my version X.Y.Z"
$ git push origin --tags
```

#### 4. Remove the no longer needed branch

If the release went successfully, you can now delete the branch:

```shell script
$ git branch -D release/my-new-feature
$ git push <remote_name> --delete release/my-new-feature-jdk11
```
