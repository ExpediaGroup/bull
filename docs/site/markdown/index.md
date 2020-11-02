<head>
    <title>About</title>
</head>

# ![bull-logo](images/BullBranding_03.png)

# Overview

This BeanUtils library is a utility library for managing Bean objects. The library offers the following components:

#### Bean Transformer: 

is a Java Bean to Java Bean transformer that recursively copies data from one object to another, it is generic, flexible, reusable, configurable, and incredibly fast.
It's the only library able to transform Mutable, Immutable, and Mixed bean without any custom configuration.

##### Features:
* support copy of immutable beans.
* support copy of mutable beans.
* support copy of hybrid beans (some fields private and some not).
* support copy of Java beans without getter and setter methods.
* support copy with Java primitive type.
* support copy with Java Collection type. e.g. `List<BeanA> => List<BeanB>`
* support copy with nested map fields. e.g. `Map<String, Map<String, String>>`
* support copy with array containing primitive types. e.g. `String[]` => `String[]`
* support copy with an array type. e.g. `BeanA[]` => `BeanB[]`
* support copy with property name mapping. e.g. `int id => int userId`
* support copy with recursion copy.
* support validation through annotations.
* support copy of beans with different field's name.
* support lambda function field transformation.
* support copy of java bean built through Builder.
* easy usage, declarative way to define the property mapping (in case of different names), or simply adding the Lombok annotations.
* allows setting the default value for all objects not existing in the source object.
* allows skipping transformation for a given set of fields.
* supports the retrieval of the value from getters if a field does not exist in the source object.
* supports the automatic conversion of primitive types.

#### Bean Validation: 

Validates a Java Bean against a set of rules can be precious, especially when we need to be sure that the object data is compliant with our expectations.
The validation works with both the default `javax.constraints` provided by Java and the custom one

##### Features:
* Java Bean validation
* Retrieve the violated constraints

#### Primitive type object automatic conversion: 

Converts a given primitive value into the given primitive type.
The supported types, in which an object can be converted (from / to), are: 

* `Byte`, `byte` or `byte[]`
* `Short` or `short`
* `Integer` or `int`
* `Long` or `long`
* `Float` or `float`
* `Double` or `double`
* `BigDecimal`
* `BigInteger`
* `Character` or `char`
* `Boolean` or `boolean`
* `String`

##### Features:
* Primitive type conversion
* Get a conversion function that converts a primitive type into the given one 
    
#### Related articles

* DZone: [How to Transform Any Type of Java Bean With BULL](https://dzone.com/articles/how-to-transform-any-type-of-java-bean-with-one-li)
* InfoQ: [How Expedia Is Getting Rid of Java Bean Transformers](https://www.infoq.com/articles/expedia-rid-of-bean-transformers/) [[EN](https://www.infoq.com/articles/expedia-rid-of-bean-transformers/)] [[PT-BR](https://www.infoq.com/br/articles/expedia-rid-of-bean-transformers/)]

