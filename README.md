<h1 align="left">
  <img width="420" alt="BULL" src="./docs/site/resources/images/BullBranding_03.png">
</h1>

## Bean Utils Light Library

BULL is a Java Bean to Java Bean transformer that recursively copies data from one object to another, it is generic, flexible, reusable, configurable and incredibly fast.
It's the only library able to transform Mutable, Immutable and Mixed bean without any custom configuration. 

## Start using

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bean-utils-library/badge.svg?subject=maven-central)](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bean-utils-library)
[![Javadocs](http://www.javadoc.io/badge/com.hotels.beans/bean-utils-library.svg)](http://www.javadoc.io/doc/com.hotels.beans/bean-utils-library)
[![Build Status](https://travis-ci.org/HotelsDotCom/bull.svg?branch=master)](https://travis-ci.org/HotelsDotCom/bull)
[![Join the chat at https://gitter.im/bean-utils-light-library/community](https://badges.gitter.im/bean-utils-light-library/community.svg)](https://gitter.im/bean-utils-light-library/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![GitHub site](https://img.shields.io/badge/GitHub-site-blue.svg)](https://hotelsdotcom.github.io/bull/)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=BULL&metric=coverage)](https://sonarcloud.io/dashboard?id=BULL)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BULL&metric=security_rating)](https://sonarcloud.io/dashboard?id=BULL)
![GitHub license](https://img.shields.io/github/license/HotelsDotCom/bull.svg)

You can obtain BULL from Maven Central: 

~~~
<dependency>
    <groupId>com.hotels.beans</groupId>
    <artifactId>bean-utils-library</artifactId>
    <version>x.y.z</version>
</dependency>
~~~

## Maven build

Full build
~~~
mvn clean install
~~~

Skip test coverage and checkstyle check
~~~
mvn clean install -P relaxed
~~~

## Features:
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
* support copy with recursion copy.
* support validation through annotations.
* support copy of beans with different field's name.
* support lambda function field transformation.
* support copy of java bean built through Builder.
* easy usage, declarative way to define the property mapping (in case of different names) or simply adding the lombok annotations.
* allows to set the default value for all objects not existing in the source object.
* allows to skip transformation for a given set of fields.

# Transformation samples

### Simple case:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ImmutableToSubFoo> nestedObjectList;                    
                                                               private ImmutableToSubFoo nestedObject;
   
   // all constructors                                         // all args constructor
   // getters and setters...                                   // getters... 
}                                                               
                                                            }
~~~
And one line code as:
~~~Java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
~~~

### Different field names copy:

From class and To class with custom annotation usage and different field names:
~~~Java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final String name;                                  private final String differentName;                   
   private final int id;                                       private final int id;                      
   private final List<FromSubBean> subBeanList;                private final List<ToSubBean> subBeanList;                 
   private final List<String> list;                            private final List<String> list;                    
   private final FromSubBean subObject;                        private final ToSubBean subObject;                    
    
   // getters and setters...
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
                                                                
                                                                    // getters and setters...           
                                              
                                                                }
~~~
And one line code as:

~~~Java                                                                
beanUtils.getTransformer().withFieldMapping(new FieldMapping("name", "differentName")).transform(fromBean, ToBean.class);                                                               
~~~

### Mapping destination fields with correspondent fields contained inside one of the nested object in the source object:

Assuming that the object `FromSubBean` is declared as follow:
~~~Java
public class FromSubBean {                         
                                                                                       
   private String serialNumber;                 
   private Date creationDate;                    
   
   // getters and setters... 
   
}
~~~
and our source object and destination object are described as follow:
~~~Java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final int id;                                       private final int id;                      
   private final String name;                                  private final String name;                   
   private final FromSubBean subObject;                        private final String serialNumber;                 
                                                               private final Date creationDate;                    
   
   // all args constructor                                     // all args constructor
   // getters...                                               // getters... 
   
}                                                           }
~~~
the fields: `serialNumber` and `creationDate` needs to be retrieved from `subObject`, this can be done defining the whole path to the end property:
~~~Java  
FieldMapping serialNumberMapping = new FieldMapping("subObject.serialNumber", "serialNumber");                                                             
FieldMapping creationDateMapping = new FieldMapping("subObject.creationDate", "creationDate");
                                                             
beanUtils.getTransformer()
         .withFieldMapping(serialNumberMapping, creationDateMapping)
         .transform(fromBean, ToBean.class);                                                               
~~~

### Different field names defining constructor args:

~~~Java
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
~~~
And one line code as:
~~~Java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
~~~

### Different field names and types applying transformation through lambda function:

~~~Java
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
~~~

~~~Java
FieldTransformer<BigInteger, BigInteger> fieldTransformer = new FieldTransformer<>("identifier", BigInteger::negate);
FieldTransformer<String, Locale> localeTransformer = new FieldTransformer<>("locale", Locale::forLanguageTag);
beanUtils.getTransformer()
    .withFieldMapping(new FieldMapping("id", "identifier"))
    .withFieldTransformer(fieldTransformer).transform(fromBean, ToBean.class)
    .withFieldTransformer(localeTransformer);
~~~

### Assign a default value in case of missing field in the source object:

Assign a default value in case of missing field in the source object:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will be null and no exceptions will be raised

   // constructors...                                          // constructors...
   // getters...                                               // getters and setters...

}                                                           }
~~~
And one line code as:
~~~Java
ToBean toBean = beanUtils.getTransformer()
                    .setDefaultValueForMissingField(true).transform(fromBean, ToBean.class);
~~~

### Applying a transformation function in case of missing fields in the source object:

Assign a default value in case of missing field in the source object:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will have value: sampleVal
                                                               
   // all args constructor                                     // constructors...
   // getters...                                               // getters and setters...
}                                                           }
~~~
And one line code as:
~~~Java
FieldTransformer<String, String> notExistingFieldTransformer = new FieldTransformer<>("notExistingField", val -> "sampleVal");
ToBean toBean = beanUtils.getTransformer()
                    .withFieldTransformer(notExistingFieldTransformer)
                    .transform(fromBean, ToBean.class);
~~~

### Apply a transformation function on a field contained in a nested object:

This example shows of a lambda transformation function can be applied on a nested object field.

Given:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    

   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
~~~
and
~~~Java
public class FromSubBean {                                  public class ToSubBean {                           
   private final String name;                                  private final String name;                   
   private final long index;                                   private final long index;                    
}                                                           }
~~~
Assuming that the lambda transformation function should be applied only to field: `name` contained into the `ToSubBean` object, the transformation function has to be defined as 
follow:
~~~Java
FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("nestedObject.name", StringUtils::capitalize);
ToBean toBean = beanUtils.getTransformer()
                    .withFieldTransformer(nameTransformer)
                    .transform(fromBean, ToBean.class);
~~~

### Apply a transformation function on all fields matching with the given one:

This example shows of a lambda transformation function can be applied on all fields matching with the given one independently from their position.

Given:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    

   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
~~~
and
~~~Java
public class FromSubBean {                                  public class ToSubBean {                           
   private final String name;                                  private final String name;                   
   private final long index;                                   private final long index;                    
   
   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
~~~
Assuming that the lambda transformation function should be applied only to field: `name` contained into the `ToSubBean` object, the transformation function has to be defined as 
follow:
~~~Java
FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("name", StringUtils::capitalize);
ToBean toBean = beanUtils.getTransformer()
                    .setFlatFieldNameTransformation(true)
                    .withFieldTransformer(nameTransformer)
                    .transform(fromBean, ToBean.class);
~~~

### Static transformer function:

~~~Java
List<FromFooSimple> fromFooSimpleList = Arrays.asList(fromFooSimple, fromFooSimple);
~~~
can be transformed as follow:
~~~Java
Function<FromFooSimple, ImmutableToFooSimple> transformerFunction = BeanUtils.getTransformer(ImmutableToFooSimple.class);
List<ImmutableToFooSimple> actual = fromFooSimpleList.stream()
                .map(transformerFunction)
                .collect(Collectors.toList());
~~~
or if you have a pre-configured transformer:
~~~Java
Function<FromFooSimple, ImmutableToFooSimple> transformerFunction = BeanUtils.getTransformer(<yourPreconfiguredTransformer>, ImmutableToFooSimple.class);
List<ImmutableToFooSimple> actual = fromFooSimpleList.stream()
                .map(transformerFunction)
                .collect(Collectors.toList());
~~~

### Disable Java Beans validation:

Assuming that the field: `id` in the fromBean instance is null.
~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;

   // all args constructor                                     // all args constructor
   // getters...                                               // getters and setters...
}                                                            }
~~~
adding the following configuration no exception will be thrown:
~~~Java
ToBean toBean = beanUtils.getTransformer()
                     .setValidationDisabled(true)
                     .transform(fromBean, ToBean.class);
~~~

### Copy on an existing instance:

Given:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private String name;                   
   private final FromSubBean nestedObject;                     private ToSubBean nestedObject;                    

   // all args constructor                                     // constructor
   // getters...                                               // getters and setters...
}                                                           }
~~~
if you need to perform the copy on an already existing object, just do:
~~~Java
ToBean toBean = new ToBean();
beanUtils.getTransformer().transform(fromBean, toBean);
~~~

### Skip transformation on a given set of fields:

Given:

~~~Java
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
~~~
if you need to skip the transformation for a given field, just do:
~~~Java
ToBean toBean = new ToBean();
beanUtils.getTransformer()
    .skipTransformationForField("nestedObject")
    .transform(fromBean, toBean);
~~~

where `nestedObject` is the name of the field in the destination object.

This feature allows to **transform an object keeping the data from different sources**.

To better explain this function let's assume that the `ToBean` (defined above) should be transformed as follow:
- `name` field value has be taken from the `FromBean` object
- `nestedObject` field value has be taken from the `FromBean2` object

the objective can be reached by doing:
~~~Java
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
~~~

More sample beans can be found in the test package: `com.hotels.beans.sample`

## Third party library comparison

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
  Workaround is introducing the Builder Pattern. An example can be found [here](http://codeslut.blogspot.com/2010/05/mapping-immutable-value-objects-with.html)_
_[+] Requires a custom configuration_
 
## Performance

Let's have a look to the performance library performance. The test has been executed on the following objects:

* Mutable objects
* Mutable objects extending another mutable object
* Immutable objects
* Immutable objects extending another immutable object
* Mixed objects

| | **Mutable**      | **Immutable** | **Mixed**       |
| :----------- | :-----------: | :-----------: | :-----------: |
| Simple objects (without nested objects) | ~0.05ms | ~0.034ms | NA |
| Complex objects (containing several nested object and several items in Map and Array objects) | ~0.38ms | ~0.21ms | ~0.22ms | 
| CPU/Heap usage | [~0.2%/35 MB](docs/site/resources/images/stats/performance/mutableObject/jvmStats.jpg) | [~0.2%/30 MB](docs/site/resources/images/stats/performance/immutableObject/jvmStats.jpg) | [~0.2%/25 MB](docs/site/resources/images/stats/performance/mixedObject/jvmStats.jpg) |

Transformation time [screenshot](docs/site/resources/images/stats/performance/transformationTime.jpg)

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

## Constraints:

* the class's fields that have to be copied must not be static
* the class must not contain builders

## Documentation

A detailed project documentation is available [here](https://hotelsdotcom.github.io/bull), including some samples for [testing the library](https://hotelsdotcom.github.io/bull/transformer/testing.html) inside your project.

## Credits

Created by: [Fabio Borriello](https://github.com/fborriello) with the contribution of: [Patrizio Munzi](https://github.com/patriziomunzi), 
[Andrea Marsiglia](https://github.com/AndreaMars94), [Giorgio Delle Grottaglie](https://github.com/geordie--) & the Hotels.com's Checkout team in Rome.

The application's logo has been designed by: Rob Light.

## Legal

This project is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

Copyright 2018-2019 Expedia Inc.