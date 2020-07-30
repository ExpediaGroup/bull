<head>
    <title>Kotlin project integration</title>
</head>

# Kotlin project integration

This page shows how to use BULL inside a Kotlin project.

## Step 1

Add the project dependency into your `pom.xml` file:

```xml
<dependency>
    <groupId>com.hotels.beans</groupId>
    <artifactId>bull-bean-transformer</artifactId>
    <version>x.y.z</version>
</dependency>
```

## Step 2

Given the following source object:

```kotlin
data class FromBean(val name: String)
```

and the following destination one:

```kotlin
data class ToBean(val name: String)
```

To transform the one into the other:

```kotlin
val fromBean = FromBean("Goofy")
BeanUtils().transformer.transform(fromBean, ToBean::class.java)
```

