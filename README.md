<h1>
  <img width="420" alt="BULL" src="./docs/site/resources/images/BullBranding_03.png">
</h1>

## Bean Utils Light Library

BULL is a Java Bean to Java Bean transformer that recursively copies data from one object to another, it is generic, flexible, reusable, configurable, and incredibly fast.
It's the only library able to transform Mutable, Immutable, and Mixed bean without any custom configuration. 

## Start using

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bull-bean-transformer/badge.svg?subject=maven-central&color=blue)](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bull-bean-transformer)
[![Javadocs](http://www.javadoc.io/badge/com.hotels.beans/bull-bean-transformer.svg?color=blue)](http://www.javadoc.io/doc/com.hotels.beans/bull-bean-transformer)
[![Build Status](https://travis-ci.com/HotelsDotCom/bull.svg?branch=master)](https://travis-ci.com/HotelsDotCom/bull)
[![Join the chat at https://join.slack.com/t/bull-crew/shared_invite/enQtNjM1MTE5ODg1MTQzLWI5ODhhYTQ2OWQxODgwYzU1ODMxMWJiZDkzODM3OTJkZjBlM2MwMTI3ZWZjMmU0OGZmN2RmNjg4NWI2NTMzOTk](https://img.shields.io/badge/chat-on%20slack-ff69b4.svg)](https://join.slack.com/t/bull-crew/shared_invite/enQtNjM1MTE5ODg1MTQzLWI5ODhhYTQ2OWQxODgwYzU1ODMxMWJiZDkzODM3OTJkZjBlM2MwMTI3ZWZjMmU0OGZmN2RmNjg4NWI2NTMzOTk)

[![GitHub site](https://img.shields.io/badge/GitHub-site-blue.svg)](https://hotelsdotcom.github.io/bull/)
[![Coverage Status](https://coveralls.io/repos/github/HotelsDotCom/bull/badge.svg?branch=master)](https://coveralls.io/github/HotelsDotCom/bull?branch=master)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BULL&metric=security_rating)](https://sonarcloud.io/dashboard?id=BULL)
![GitHub license](https://img.shields.io/github/license/HotelsDotCom/bull.svg)
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=HotelsDotCom/bull)](https://dependabot.com)

All BULL modules are available on Maven Central: 

* ### Bean Transformer

```xml
<dependency>
    <groupId>com.hotels.beans</groupId>
    <artifactId>bull-bean-transformer</artifactId>
    <version>x.y.z</version>
</dependency>
```

* ### `Map` Transformer

```xml
<dependency>
    <groupId>com.hotels.beans</groupId>
    <artifactId>bull-map-transformer</artifactId>
    <version>x.y.z</version>
</dependency>
```

**The project provides two different builds**, one compatible with `jdk 8` (or above) and one with `jdk 11` or above.

In case you need to integrate it in a `jdk 8` (or above project) please refer to [CHANGELOG-JDK8](CHANGELOG-JDK8.md) file or [CHANGELOG](CHANGELOG.md) otherwise.

* #### Suggestions

Some jdk versions remove the Java Bean constructor's argument names from the compiled code and this may cause problems to the library.
On top of that, it's suggested to configure the `maven-compiler-plugin`, inside your project, as follow:

```xml
<build>
    ...
    <pluginManagement>
        <plugins>
            ...
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven.compiler.plugin.version}</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <parameters>true</parameters>
                    <forceJavacCompilerUse>true</forceJavacCompilerUse>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
    ...
</build>
```

## Maven build

Full build
```shell script
./mvnw clean install
```
or on Windows
```shell script
mvnw.cmd clean install
```

Skip test coverage and checkstyle check
```shell script
./mvnw clean install -P relaxed
```
or on Windows
```shell script
mvnw.cmd clean install -P relaxed
```

## Features:
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

# Feature samples

* [Bean Transformation](https://github.com/HotelsDotCom/bull#bean-transformation-samples)
* [Bean Validation](https://github.com/HotelsDotCom/bull#validation-samples)
* [Primitive Type conversion](https://github.com/HotelsDotCom/bull#primitive-type-object-converter)
* [Map Transformation](https://hotelsdotcom.github.io/bull/transformer/map/samples.html)
* [Supported Builder Pattern](https://hotelsdotcom.github.io/bull/transformer/bean/builder.html)
* [How to use it in Kotlin](https://hotelsdotcom.github.io/bull/kotlin.html)

## Bean transformation samples

### Simple case:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ToSubBean> subBeanList;                    
                                                               private ImmutableToSubFoo subObject;
   
   // all constructors                                         // all args constructor
   // getters and setters...                                   // getters and setters... 
}    
```
And one line code as:
```java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
```

### Different field names copy:

From class and To class with different field names:
```java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final String name;                                  private final String differentName;                   
   private final int id;                                       private final int id;                      
   private final List<FromSubBean> subBeanList;                private final List<ToSubBean> subBeanList;                 
   private final List<String> list;                            private final List<String> list;                    
   private final FromSubBean subObject;                        private final ToSubBean subObject;                    
    
   // getters...
                                                               public ToBean(final String differentName, 
                                                                        final int id,
}                                                                       final List<ToSubBean> subBeanList,
                                                                        final List<String> list,
                                                                        final ToSubBean subObject) {
                                                                        this.differentName = differentName;
                                                                        this.id = id;
                                                                        this.subBeanList = subBeanList;
                                                                        this.list = list;
                                                                        this.subObject = subObject; 
                                                                    }
                                                                
                                                                    // getters...           
                                              
                                                                }
```
And one line code as:

```java                                                                
beanUtils.getTransformer().withFieldMapping(new FieldMapping<>("name", "differentName")).transform(fromBean, ToBean.class);                                                               
```

### Mapping destination fields with correspondent fields contained inside one of the nested objects in the source object:

Assuming that the object `FromSubBean` is declared as follow:
```java
public class FromSubBean {                         
                                                                                       
   private String serialNumber;                 
   private Date creationDate;                    
   
   // getters and setters... 
   
}
```
and our source object and destination object are described as follow:
```java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final int id;                                       private final int id;                      
   private final String name;                                  private final String name;                   
   private final FromSubBean subObject;                        private final String serialNumber;                 
                                                               private final Date creationDate;                    
   
   // all args constructor                                     // all args constructor
   // getters...                                               // getters... 
   
}                                                           }
```
the fields: `serialNumber` and `creationDate` needs to be retrieved from `subObject`, this can be done defining the whole path to the end property:
```java  
FieldMapping serialNumberMapping = new FieldMapping<>("subObject.serialNumber", "serialNumber");                                                             
FieldMapping creationDateMapping = new FieldMapping<>("subObject.creationDate", "creationDate");
                                                             
beanUtils.getTransformer()
         .withFieldMapping(serialNumberMapping, creationDateMapping)
         .transform(fromBean, ToBean.class);                                                               
```

### Different field names defining constructor args:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String differentName;                   
   private final int id;                                       private final int id;                      
   private final List<FromSubBean> subBeanList;                private final List<ToSubBean> subBeanList;                 
   private final List<String> list;                            private final List<String> list;                    
   private final FromSubBean subObject;                        private final ToSubBean subObject;                    
   
   // all args constructor
   // getters...
                                                               public ToBean(@ConstructorArg("name") final String differentName, 
                                                                        @ConstructorArg("id") final int id,
}                                                                       @ConstructorArg("subBeanList") final List<ToSubBean> subBeanList,
                                                                        @ConstructorArg(fieldName ="list") final List<String> list,
                                                                        @ConstructorArg("subObject") final ToSubBean subObject) {
                                                                        this.differentName = differentName;
                                                                        this.id = id;
                                                                        this.subBeanList = subBeanList;
                                                                        this.list = list;
                                                                        this.subObject = subObject; 
                                                                    }
                                                                
                                                                    // getters...           
                                              
                                                            }
```
And one line code as:
```java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
```

### Different field names and types applying transformation through lambda function:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger identifier;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ImmutableToSubFoo> nestedObjectList;                    
   private final String locale;                                private final Locale locale;                    
                                                               private ImmutableToSubFoo nestedObject;
       
   // constructors...                                          // constructors...
   // getters and setters...                                   // getters and setters...
                                                                                                                              
}                                                           }
```

```java
FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", BigInteger::negate);
FieldTransformer<String, Locale> localeTransformer = new FieldTransformer<>("locale", Locale::forLanguageTag);
beanUtils.getTransformer()
    .withFieldMapping(new FieldMapping<>("id", "identifier"))
    .withFieldTransformer(fieldTransformer).transform(fromBean, ToBean.class)
    .withFieldTransformer(localeTransformer);
```

### Assign a default value in case of missing field in the source object:

Assign a default value in case of missing field in the source object:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will be null and no exceptions will be raised

   // constructors...                                          // constructors...
   // getters...                                               // getters and setters...

}                                                           }
```
And one line code as:
```java
ToBean toBean = beanUtils.getTransformer()
                    .setDefaultValueForMissingField(true).transform(fromBean, ToBean.class);
```

### Disable the default value set for primitive types in case they are null:

BULL by default sets the default value for all primitive types fields in case their value in the source object.
Given the following Java Bean:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 

   // constructors...                                          // constructors...
   // getters...                                               // getters and setters...

}                                                           }
```

in case the field `id` in the `FromBean` object is `null`, the value assigned the correspondent field in the `ToBean` object will be `0`.
To disable this you can simply do: 

```java
ToBean toBean = beanUtils.getTransformer()
                    .setDefaultValueForMissingPrimitiveField(false).transform(fromBean, ToBean.class);
```

in this case, the field `id` after the transformation will be `null`

### Applying a transformation function in case of missing fields in the source object:

Assign a default value in case of missing field in the source object:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will have value: sampleVal
                                                               
   // all args constructor                                     // constructors...
   // getters...                                               // getters and setters...
}                                                           }
```
And one line code as:
```java
FieldTransformer<String, String> notExistingFieldTransformer = new FieldTransformer<>("notExistingField", () -> "sampleVal");
ToBean toBean = beanUtils.getTransformer()
                    .withFieldTransformer(notExistingFieldTransformer)
                    .transform(fromBean, ToBean.class);
```

### Apply a transformation function on a field contained in a nested object:

This example shows off a lambda transformation function that can be applied to a nested object field.

Given:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    

   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
```
and
```java
public class ToSubBean {                           
   private final String name;                   
   private final long index;                    
}
```
Assuming that the lambda transformation function should be applied only to field: `name` contained into the `ToSubBean` object, the transformation function has to be defined as 
follow:
```java
FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("nestedObject.name", StringUtils::capitalize);
ToBean toBean = beanUtils.getTransformer()
                    .withFieldTransformer(nameTransformer)
                    .transform(fromBean, ToBean.class);
```

### Map a primitive type field in the source object into a nested object:

This example shows how to map a primitive field into a nested object into the destination one.

Given:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    
   private final int x;
   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
```
and
```java
public class ToSubBean {                           
   private final int x;
   
   // all args constructor
}  // getters...          
```
Assuming that the value `x` should be mapped into the field: `x` contained into the `ToSubBean` object, the field mapping has to be defined as 
follow:
```java
ToBean toBean = beanUtils.getTransformer()
                    .withFieldMapping(new FieldMapping<>("x", "nestedObject.x"));
```

### Apply a transformation function on all fields matching with the given one:

This example shows how a lambda transformation function can be applied to all fields matching with the given one independently from their position.

Given:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    

   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
```
and
```java
public class FromSubBean {                                  public class ToSubBean {                           
   private final String name;                                  private final String name;                   
   private final long index;                                   private final long index;                    
   
   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
```
Assuming that the lambda transformation function should be applied only to the field: `name` contained in the `ToSubBean` object, the transformation function has to be defined
 as 
follow:
```java
FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("name", StringUtils::capitalize);
ToBean toBean = beanUtils.getTransformer()
                    .setFlatFieldNameTransformation(true)
                    .withFieldTransformer(nameTransformer)
                    .transform(fromBean, ToBean.class);
```

### Static transformer function:

```java
List<FromFooSimple> fromFooSimpleList = Arrays.asList(fromFooSimple, fromFooSimple);
```
can be transformed as follow:
```java
Function<FromFooSimple, ImmutableToFooSimple> transformerFunction = BeanUtils.getTransformer(ImmutableToFooSimple.class);
List<ImmutableToFooSimple> actual = fromFooSimpleList.stream()
                .map(transformerFunction)
                .collect(Collectors.toList());
```
or if you have a pre-configured transformer:
```java
Function<FromFooSimple, ImmutableToFooSimple> transformerFunction = BeanUtils.getTransformer(<yourPreconfiguredTransformer>, ImmutableToFooSimple.class);
List<ImmutableToFooSimple> actual = fromFooSimpleList.stream()
                .map(transformerFunction)
                .collect(Collectors.toList());
```

### Enable Java Beans validation:

Assuming that the field: `id` in the fromBean instance is null.
```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;

   // all args constructor                                     // all args constructor
   // getters...                                               // getters and setters...
}                                                            }
```
adding the following configuration an exception will be thrown:
```java
ToBean toBean = beanUtils.getTransformer()
                     .setValidationEnabled(true)
                     .transform(fromBean, ToBean.class);
```

### Copy on an existing instance:

Given:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private String name;                   
   private final FromSubBean nestedObject;                     private ToSubBean nestedObject;                    

   // all args constructor                                     // constructor
   // getters...                                               // getters and setters...
}                                                           }
```
if you need to perform the copy on an already existing object, just do:
```java
ToBean toBean = new ToBean();
beanUtils.getTransformer().transform(fromBean, toBean);
```

### Skip transformation on a given set of fields:

Given:

```java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private String name;                   
   private final FromSubBean nestedObject;                     private ToSubBean nestedObject;                    

   // all args constructor                                     // constructor
   // getters...                                               // getters and setters...
}                                                           }

public class FromBean2 {                   
   private final int index;             
   private final FromSubBean nestedObject;
                                          
   // all args constructor                
   // getters...                          
}                                         
```
if you need to skip the transformation for a given field, just do:
```java
ToBean toBean = new ToBean();
beanUtils.getTransformer()
    .skipTransformationForField("nestedObject")
    .transform(fromBean, toBean);
```

where `nestedObject` is the name of the field in the destination object.

This feature allows us to **transform an object keeping the data from different sources**.

To better explain this function let's assume that the `ToBean` (defined above) should be transformed as follow:
- `name` field value has been taken from the `FromBean` object
- `nestedObject` field value has been taken from the `FromBean2` object

the objective can be reached by doing:
```java
// create the destination object
ToBean toBean = new ToBean();

// execute the first transformation skipping the copy of: 'nestedObject' field that should come from the other source object
beanUtils.getTransformer()
    .skipTransformationForField("nestedObject")
    .transform(fromBean, toBean);

// then execute the transformation skipping the copy of: 'name' field that should come from the other source object
beanUtils.getTransformer()
    .skipTransformationForField("name")
    .transform(fromBean2, toBean);
```

### Not existing field in the source object:
In case the destination class has a field that does not exist in the source object, but it contains a getter method returning the value, the library should gets the field value from that method.
```java
public class FromBean {                                     public class ToBean {                           
                                                               private final BigInteger id;
    public BigInteger getId() {                                   
        return BigInteger.TEN;                                 // all args constructor
   }                                                           // getters...
}                                                               
                                                            }
```
And one line code as:
```java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
```

### Transform primitive types automatically

Given the following Java Bean:

```java
public class FromBean {                                     public class ToBean {                           
   private final String indexNumber;                           private final int indexNumber;                                 
   private final BigInteger id;                                public Long id;                      

   // constructors...                                          // constructors...
   // getters...                                               // getters and setters...

}                                                           }
```

as, by default the primitive type conversion is disabled, to get the above object converted we should have
implemented transformer functions for both field `indexNumber` and `id`, but this can be done automatically by enabling the
the functionality described above.

```java
Transformer transformer = beanUtils.getTransformer()
                             .setPrimitiveTypeConversionEnabled(true);

ToBean toBean = transformer.transform(fromBean, ToBean.class);
```

**IMPORTANT:** The primitive type transformation (if enabled) is executed before any other `FieldTransformer` function defined on a specific field.
This means that once the `FieldTransformer` function will be executed the field value has already been transformed.

## Builder supported patterns

The library support the transformation of Java Bean using the following Builder patterns:

### Standard pattern:

```java
public class ItemType {
    private final Class<?> objectClass;
    private final Class<?> genericClass;

    ItemType(final Class<?> objectClass, final Class<?> genericClass) {
        this.objectClass = objectClass;
        this.genericClass = genericClass;
    }

    public static ItemTypeBuilder builder() {
        return new ItemType.ItemTypeBuilder();
    }

    // getter methods

    public static class ItemTypeBuilder {
        private Class<?> objectClass;
        private Class<?> genericClass;

        ItemTypeBuilder() {
        }

        public ItemTypeBuilder objectClass(final Class<?> objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        public ItemTypeBuilder genericClass(final Class<?> genericClass) {
            this.genericClass = genericClass;
            return this;
        }

        public com.hotels.transformer.model.ItemType build() {
            return new ItemType(this.objectClass, this.genericClass);
        }
    }
}
```

### Custom Builder pattern:

To enable the transformation of Java Beans using the following Builder pattern:

```java
public class ItemType {
    private final Class<?> objectClass;
    private final Class<?> genericClass;

    ItemType(final ItemTypeBuilder builder) {
        this.objectClass = builder.objectClass;
        this.genericClass = builder.genericClass;
    }

    public static ItemTypeBuilder builder() {
        return new ItemType.ItemTypeBuilder();
    }

    // getter methods

    public static class ItemTypeBuilder {
        private Class<?> objectClass;
        private Class<?> genericClass;

        ItemTypeBuilder() {
        }

        public ItemTypeBuilder objectClass(final Class<?> objectClass) {
            this.objectClass = objectClass;
            return this;
        }

        public ItemTypeBuilder genericClass(final Class<?> genericClass) {
            this.genericClass = genericClass;
            return this;
        }

        public com.hotels.transformer.model.ItemType build() {
            return new ItemType(this);
        }
    }
}
```

It's needed to enable the custom Builder Transformation as following:

```java
ToBean toBean = new BeanTransformer()
                         .setCustomBuilderTransformationEnabled(true)
                         .transform(sourceObject, ToBean.class);
```

## Constraints:

* the class's fields that have to be copied must not be static

More sample beans can be found in the test package: `com.hotels.beans.sample`

## Third-party library comparison

Following a comparison between the BULL functionalities and the following Third-Party libraries:

* [Apache BeanUtils](http://commons.apache.org/proper/commons-beanutils/)
* [Jackson Data Bind](https://github.com/FasterXML/jackson-databind)
* [Dozer](http://dozer.sourceforge.net/)

| | **BULL**      | **Apache Bean Utils** | **Jackson**       | **Dozer** |
| :----------- | :-----------: | :-----------: | :-----------: | :-----------: |
| Mutable bean copy | X | X | X | X+ |
| Mutable bean with nested objects | X | - | X | X+ |
| Mutable bean extending classes | X | - | X | X+ |
| Immutable bean copy | X | - | - | X* |
| Mixed bean copy | X | - | - | X+ |
| Copy of beans without getter and setter methods defined | X | - | - | - |
| Mutable Bean with different field's name  | X | - | - | X+ |
| Mixed with different field's type | X | - | - | X+ |
| Immutable with different field's type | X | - | - | X+ |
| Mutable Bean containing collection type fields containing complex objects | X | - | X | X |
| Mixed Bean containing collection type fields containing complex objects | X | - | - | X+ |
| Immutable Bean containing collection type fields containing complex objects | X | - | - | X+ |
| Mutable Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>` | X | - | X | X |
| Mixed Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>` | X | - | - | X+ |
| Immutable Bean containing containing Map type fields with nested Maps inside.  e.g. `Map<String, Map<String, Integer>>` | X | - | - | X+ |
| Annotation field validation | X | - | X | - |

_[*] Immutable types are not supported by Dozer. When a type doesn't have a no-arg constructor and all fields are final, Dozer can't perform the mapping.
  A workaround is introducing the Builder Pattern. An example can be found [here](http://codeslut.blogspot.com/2010/05/mapping-immutable-value-objects-with.html)_
_[+] Requires a custom configuration_
 
## Performance

Let's have a look at the performance library performance. The test has been executed on the following objects:

* Mutable objects
* Mutable objects extending another mutable object
* Immutable objects
* Immutable objects extending another immutable object
* Mixed objects

| | **Mutable**      | **Immutable** | **Mixed**       |
| :----------- | :-----------: | :-----------: | :-----------: |
| Simple objects (without nested objects) | ~0.011ms | ~0.018ms | NA |
| Complex objects (containing several nested object and several items in Map and Array objects) | ~0.37ms | ~0.21ms | ~0.22ms | 
| CPU/Heap usage | [~0.2%/35 MB](docs/site/resources/images/stats/performance/mutableObject/jvmStats.jpg) | [~0.2%/30 MB](docs/site/resources/images/stats/performance/immutableObject/jvmStats.jpg) | [~0.2%/25 MB](docs/site/resources/images/stats/performance/mixedObject/jvmStats.jpg) |

Transformation time [screenshot](docs/site/resources/images/stats/performance/transformationTime.png)

#### Real case testing

The Bean Utils library has been tested on a real case scenario integrating it into a real edge service (called BPE).
The purpose was to compare the latency introduced by the library plus the memory/CPU usage.
The dashboard's screenshot shows the latency of the invoked downstream service (called BPAS) and the one where the library has been installed (BPE). 
Following the obtained results:

| | **Classic transformer** | **BeanUtils library** |
| :----------- | :-----------: | :-----------: |
| Throughput per second | 60 | 60 |
| Average CPU usage | 0.3% | 0.3% |
| Min/Max Heap Memory Usage (MB) | 90/320 | 90/320 |
| Average Latency than the downstream service | +2ms | +2ms |
| JVM stats screenshot | [screenshot](docs/site/resources/images/stats/performance/realTestCase/classicTransformer/jvmStats.jpg) | [screenshot](docs/site/resources/images/stats/performance/realTestCase/beanUtilsLib/jvmStats.jpg) |
| Dashboard screenshot | [screenshot](docs/site/resources/images/stats/performance/realTestCase/classicTransformer/dashboard.jpg) | [screenshot](docs/site/resources/images/stats/performance/realTestCase/beanUtilsLib/dashboard.jpg) |

## Validation samples

Validate a java bean has never been so simple. The library offers different API related to this, following some examples:

### Validate a Java Bean:

Given the following bean:

```java
public class SampleBean {                           
   @NotNull                   
   private BigInteger id;                      
   private String name;                 
   
   // constructor
   // getters and setters... 
}                                                               
```

an instance of the above object:

```java
SampleBean sampleBean = new SampleBean();
```

And one line code as:

```java
beanUtils.getValidator().validate(sampleBean);
```

this will throw an `InvalidBeanException` as the `id` field is null.

### Retrieve the violated constraints:

Given the following bean:

```java
public class SampleBean {                           
   @NotNull                   
   private BigInteger id;                      
   private String name;                 
   
   // constructor
   // getters and setters... 
}                                                               
```

an instance of the above object:

```java
SampleBean sampleBean = new SampleBean();
```

And one line code as:

```java
List<String> violatedConstraints = beanUtils.getValidator().getConstraintViolationsMessages(sampleBean);
```

this will returns a list containing a constraint validation message for the `id` field as it's null and the constraint: `@NotNull` is not met.

in case it's needed to have the `ConstraintViolation` object:
```java
Set<ConstraintViolation<Object>> violatedConstraints = beanUtils.getValidator().getConstraintViolations(sampleBean);
```

## Primitive type object converter

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

### Convert a String into an int:

Given the following variable:

```java
String indexNumber = "26062019";                                                        
```

to convert it in an `int`:

```java
Converter converter = new BeanUtils().getPrimitiveTypeConverter();
int indexNumber = converter.convertValue(indexNumber, int.class);
```

### Obtain a conversion function that converts from char to byte:

It's possible to obtain a type conversion function, reusable several times in different places.
Assuming that the required conversion is from `char` to `byte

```java
char c = '1';                                                        
```

the conversion function is retrieved through:

```java
Converter converter = new BeanUtils().getPrimitiveTypeConverter();
Optional<Function<Object, Object>> conversionFunction = converter.getConversionFunction(char.class, byte.class);
byte converted = conversionFunction.map(processor -> processor.apply(c)).orElse(0);
```

* in case the conversion is not needed as the primitive type and the destination type are the same it will return an empty `Optional`
* in case the conversion function is unavailable or no not possible the method throws a: `TypeConversionException`

## `Map` transformation samples

Samples on how to transform a `Map` and all others function applicable to it can be viewed [here](https://hotelsdotcom.github.io/bull/transformer/mapTransformer.html)

## Documentation

Detailed project documentation is available [here](https://hotelsdotcom.github.io/bull), including some samples for [testing the library](https://hotelsdotcom.github.io/bull/transformer/testing.html) inside your project.

An article that explains how it works, with suggestion and examples is available on DZone: [How to Transform Any Type of Java Bean With BULL](https://dzone.com/articles/how-to-transform-any-type-of-java-bean-with-one-li) 

## Credits

Created by: [Fabio Borriello](https://github.com/fborriello) with the contribution of: [Patrizio Munzi](https://github.com/patriziomunzi), 
[Andrea Marsiglia](https://github.com/AndreaMars94), [Giorgio Delle Grottaglie](https://github.com/geordie--) & the Hotels.com's Checkout team in Rome.

The application's logo has been designed by Rob Light.

## Related articles

 - DZone: [How to Transform Any Type of Java Bean With BULL](https://dzone.com/articles/how-to-transform-any-type-of-java-bean-with-one-li)
 - InfoQ: [How Expedia Is Getting Rid of Java Bean Transformers](https://www.infoq.com/articles/expedia-rid-of-bean-transformers/) [[EN](https://www.infoq.com/articles/expedia-rid-of-bean-transformers/)] [[PT-BR](https://www.infoq.com/br/articles/expedia-rid-of-bean-transformers/)]

## Release

All the instructions for releasing a new version are available at [RELEASES.md](RELEASE.md)

## Badge your project

[![Bull enabled](https://img.shields.io/badge/bull-enabled-red)](https://github.com/HotelsDotCom/bull)

Add the following snippet in your Markdown file:

```
[![Bull enabled](https://img.shields.io/badge/bull-enabled-red)](https://github.com/HotelsDotCom/bull)
```

## Support

[![Join the chat at https://join.slack.com/t/bull-crew/shared_invite/enQtNjM1MTE5ODg1MTQzLWI5ODhhYTQ2OWQxODgwYzU1ODMxMWJiZDkzODM3OTJkZjBlM2MwMTI3ZWZjMmU0OGZmN2RmNjg4NWI2NTMzOTk](https://img.shields.io/badge/chat-on%20slack-ff69b4.svg)](https://join.slack.com/t/bull-crew/shared_invite/enQtNjM1MTE5ODg1MTQzLWI5ODhhYTQ2OWQxODgwYzU1ODMxMWJiZDkzODM3OTJkZjBlM2MwMTI3ZWZjMmU0OGZmN2RmNjg4NWI2NTMzOTk)

For any question, proposal, or help, please refer to the slack channel: [#bull](https://join.slack.com/t/bull-crew/shared_invite/enQtNjM1MTE5ODg1MTQzLWI5ODhhYTQ2OWQxODgwYzU1ODMxMWJiZDkzODM3OTJkZjBlM2MwMTI3ZWZjMmU0OGZmN2RmNjg4NWI2NTMzOTk).

## Legal

This project is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

Copyright 2018-2019 Expedia Inc.
