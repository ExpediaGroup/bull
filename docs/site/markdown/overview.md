<head>
    <title>Overview</title>
</head>

# Overview

This BeanUtils library is a utility library for managing Bean objects. The library offers the following components:


- #### Bean Transformer: 

    is a Java bean copy utility with powerful functionality and high performance.

    ##### Features:
    * support copy of immutable beans.
    * support copy of mutable beans.
    * support copy of hybrid beans (some fields private and some not).
    * support copy of Java beans without getter and setter methods.
    * support copy with Java primitive type.
    * support copy with Java Collection type. e.g. `List<BeanA> => List<BeanB>`
    * support copy with nested map fields. e.g. `Map<String, Map<String, String>>`
    * support copy with array containing primitive types. e.g. `String[]` => `String[]`
    * support copy with array type. e.g. `BeanA[]` => `BeanB[]`
    * support copy with property name mapping. e.g. `int id => int userId`
    * support copy with recursion copy
    * support validation through annotations
    * support copy of beans with different field's name
    * support lambda function field transformation
    * easy usage, declarative way to define the property mapping (in case of different names) or simply adding the lombok annotations.
