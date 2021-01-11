Contributing [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/dwyl/esta/issues)
============
If you want to contribute to the project and make it better, your help is very welcome. Contributing is also a great way to learn more about social coding on Github, new technologies and their ecosystems, and how to make constructive, helpful bug reports, feature requests, and the noblest of all contributions: a good, clean pull request.

### How to make a clean pull request

- Create a personal fork of the project on Github.
- Clone the fork on your local machine. Your remote repo on Github is called `origin`.
- Add the original repository as a remote called `upstream`.
- If you created your fork a while ago be sure to pull upstream changes into your local repository.
- Create a new branch to work on! A branch from `develop` if it exists, else from `master`.
- **The branch name should follow the best practices naming convention:**
    1. Use grouping tokens (words) at the beginning of your branch names.
        * `feature`: Feature I'm adding or expanding
        * `bug`: Bugfix or experiment
        * `wip`: Work in progress; stuff I know won't be finished soon
    2. Define and use short lead tokens to differentiate branches in a way that is meaningful to your workflow.
    3. Use slashes to separate parts of your branch names.
    4. Do not use bare numbers as leading parts.
    5. Avoid long descriptive names for long-lived branches.
- Implement/fix your feature, comment on your code.
- Follow the code style of the project, including indentation.
- If the project has tests run them!
- Write or adapt tests as needed.
- Add or change the documentation as needed.
- Squash your commits into a single commit with git's [interactive rebase](https://help.github.com/articles/interactive-rebase). Create a new branch if necessary.
- Push your branch to your fork on Github, the remote `origin`.
- From your fork open a pull request in the correct branch. Target the project's `develop` branch if there is one, else go for `master`!
- If the maintainer requests further change just push them to your branch. The pull request will be updated automatically.
- Once the pull request is approved and merged you can [pull the changes from upstream](https://help.github.com/articles/syncing-a-fork/) to your local repo and delete your extra branch(es).

And last but not least: Always write your commit messages in the present tense. Your commit message should describe what the commit when applied, does to the code – not what you
did to the code.

### Testing

- The new code should be tested enough to meet the expected coverage
- The default libraries for testing implementations are:
    1. AssertJ for the assertion
    2. Mockito for the object mocking
    3. Testng for testing annotations and data provider
