# Release process for BULL

## Explanation of the process sequence

Assumption that the current state of *master* will constitute the release...

#### 1. Pre-release checks

Make the following checks before performing a release:
   * Do all unit tests pass?
   * Do all examples work?
   * Does documentation build?
   * Have the new features been applied on a separate branch with compatibility with `jdk8?

#### 2. Update VERSION and CHANGELOG

#### Increment the version number.

The application is released with the compatibility for both: `jdk11` and `jdk8`, the latest version number are:
in the [CHANGELOG](CHANGELOG.md) file for `jdk11` and [CHANGELOG-JDK](CHANGELOG-JDK8.md) file for `jdk8`. 

The version number structure is: *major* **.** *minor* **.** *revision*.
   * *major* = significant incompatible change (e.g. partial or whole rewrite).
   * *minor* = some new functionality or changes that are mostly/wholly backward compatible.
   * *revision* = very minor changes, e.g. bugfixes.
   
For `jdk8` only, the version number is the same as the `jdk11` one, with the suffix: `-jdk8`.

e.g. if the new release version would be `1.8.0` then the `jdk8` correspondent one is: `1.8.0-jdk8

#### Update the change log

The changes, that are going to be released, needs to be specified in the `CHANGELOG` file.
Ensure it mentions any noteworthy changes since the previous release.

Make the following check before performing a release:
* Add the version is going to be released
* Place the release date next to the version number (the format is: `yyyy.mm.dd`)
* Specify under the `### Added` label everything that has been introduced by the new version 
* Specify under the `### Changed` label everything that has been changed in the new version 
* Specify under the `### Removed` label everything that has been removed in the new version 

the same changes must be reported in [CHANGELOG-JDK](CHANGELOG-JDK8.md)

#### 3. Prepare the jdk8 release

All the changes implemented needs to be reported to the `jdk8` compatible version.
The BULL code for the `jdk8` is slightly different so all the changes needs to be reported on the other version starting
from it's latest release tag.
The first thing to do is to create a branch (that would have the same name as the `jdk11` one plus the suffix: `-jd8`)
starting from the latest `jdk8` release tag:

~~~
$ git checkout -b [branch name]-jdk8 [latest jdk8 release tag] 
~~~

e.g. if the latest `jdk8` release tag is: `1.7.0-jdk8` and the new feature branch is: `feature/my-new-feature`
the command to perform is: 

~~~
$ git checkout -b feature/my-new-feature-jdk8 1.7.0-jdk8 
~~~

**IMPORTANT:** In the new branch, apply only the changes introduced comparing the code with the `jdk11` branch.

when completed, commit your code and verify that the [Travis build](https://travis-ci.org/HotelsDotCom/bull/builds) is green. 

## Example of release process sequence

The following examples assume that your local repository is:

* a clone and the working copy is currently at the head of the master branch
* is all synced with GitHub
* the Pull Request has been approved and merged on master

### JDK8

The following steps will do a release "`X.Y.Z-jdk8`"

~~~
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean
~~~

#### 1. Create a branch from the latest JDK8 release tag

Assuming that:

* the latest release for `jdk8` was: `A.B.C-jdk8` 
* your new feature branch is: `feature/my-new-feature`
* the new release version is: `X.Y.Z-jdk8`

the release branch would be: `release/my-new-feature-jdk8`

~~~
$ git checkout -b release/my-new-feature-jdk8 A.B.C-jdk8 
~~~

#### 2. Apply the changes to the new branch

Apply all the changes you implemented to this branch. 

#### 3. Change the maven version

The maven version is now set with the latest released, but you need to change it with the new one you are going to release:

~~~
$ mvn versions:set -D newVersion=X.Y.Z-jdk8
~~~

Commit all the changes.

#### 4. Create a new tag for the release version

Once you will have created the tag, and pushed it to the remote repo, an automatic release will be performed by Travis.

~~~
$ git tag -a X.Y.Z-jdk8 -m "my version X.Y.Z-jdk8"
$ git push origin --tags
~~~

#### 5. Remove the no longer needed branch

If the release went successfully, you can now delete the branch:

~~~
$ git branch -D release/my-new-feature-jdk8
$ git push <remote_name> --delete release/my-new-feature-jdk8
~~~

**IMPORTANT:** In case something goes wrong, do not leave ghost tags or tags not related to a successful release.

### JDK11

The following steps will do a release "`X.Y.Z`"

~~~
$ git status
On branch master
Your branch is up-to-date with 'origin/master'.
nothing to commit, working directory clean
~~~

#### 1. Create a branch from the master branch

Assuming that:

* the latest release for `jdk11` was: `A.B.C` 
* your new feature branch is: `feature/my-new-feature`
* the new release version is: `X.Y.Z`

the release branch would be: `release/my-new-feature`

~~~
$ git checkout -b release/my-new-feature
~~~

#### 2. Change the maven version

The maven version is now set with the latest released, but you need to change it with the new one you are going to release:

~~~
$ mvn versions:set -D newVersion=X.Y.Z
~~~

Commit all the changes.

#### 3. Create a new tag for the release version

Once you will have created the tag, and pushed it to the remote repo, an automatic release will be performed by Travis.

~~~
$ git tag -a X.Y.Z -m "my version X.Y.Z"
$ git push origin --tags
~~~

#### 4. Remove the no longer needed branch

If the release went successfully, you can now delete the branch:

~~~
$ git branch -D release/my-new-feature
$ git push <remote_name> --delete release/my-new-feature-jdk8
~~~

**IMPORTANT:** In case something goes wrong, do not leave ghost tags or tags not related to a successful release.