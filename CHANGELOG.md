
# BULL Change Log

All notable changes to this project will be documented in this file.

### [2.2.0] 2022.01.10
#### Changed
* Updated `hibernate-validator` version to `6.2.1.Final` (was `7.0.1.Final`). This replaces the jakarta validation with the javax one.

### [2.1.2] 2022.01.05
#### Changed
* Removes Java Records objects to avoid hosting projects to being forced to use the `enable-preview` options
 
### [2.1.1] 2021.10.24
#### Added
* Adds a function to reset all the defined settings
* Adds the possibility to map the same source field into multiple fields
* Adds the possibility to apply the same field transformation function on multiple fields
#### Changed
* Updated `eg-oss-parent` version to `2.4.1` (was `2.4.0`).

### [2.1.0] 2021.07.23
#### Changed
* Renames the package from `com.hotels` to `com.expediagroup`

### [2.0.1.1] 2021.06.24
#### Added
* Adds the javadoc generation to the release

### [2.0.1] 2021.06.23
#### Changed
* Fixes an issue that was preventing the transformation of Object type fields

### [2.0.0] 2021.06.18
#### Added
* Increase the jdk version to 15
* Enables the [Java Record](https://blogs.oracle.com/javamagazine/records-come-to-java) transformation