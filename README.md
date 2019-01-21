<h1 align="left">
  <img width="420" alt="BULL" src="./docs/site/resources/images/BullBranding_03.png">
</h1>

## Bean Utils Light Library

This BeanUtils library is a Java Bean to Java Bean transformer that recursively copies data from one object to another, it is generic, flexible, reusable, configurable and incredibly fast.
It's the only library able to transform Mutable, Immutable and Mixed bean without any custom configuration. 

## Start using

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bean-utils-library/badge.svg?subject=com.hotels.beans:bean-utils-library)](https://maven-badges.herokuapp.com/maven-central/com.hotels.beans/bean-utils-library)
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
* support copy with recursion copy
* support validation through annotations
* support copy of beans with different field's name
* support lambda function field transformation
* easy usage, declarative way to define the property mapping (in case of different names) or simply adding the lombok annotations.
* allows to set the default value for all objects not existing in the source object.

## Transformation samples

### Simple case:

~~~Java
@AllArgsConstructor                                         @AllArgsConstructor
@Getter                                                     @Getter
@Setter                                                     @Setter
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ImmutableToSubFoo> nestedObjectList;                    
                                                               private ImmutableToSubFoo nestedObject;
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
                                                               public ToBean(final String name, 
                                                                        final int id,
}                                                                       final List<ToSubBean> subBeanList,
                                                                        final List<String> list,
                                                                        final ToSubBean subObject) {
                                                                        this.name = name;
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
beanUtils.getTranformer().withFieldMapping(new FieldMapping("id", "identifier")).transform(fromBean, ToBean.class);                                                               
~~~

### Different field names defining constructor args:

~~~Java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final String name;                                  private final String differentName;                   
   private final int id;                                       private final int id;                      
   private final List<FromSubBean> subBeanList;                private final List<ToSubBean> subBeanList;                 
   private final List<String> list;                            private final List<String> list;                    
   private final FromSubBean subObject;                        private final ToSubBean subObject;                    
    
   // getters and setters...
                                                               public ToBean(@ConstructorArg("name") final String name, 
                                                                        @ConstructorArg("id") final int id,
}                                                                       @ConstructorArg("subBeanList") final List<ToSubBean> subBeanList,
                                                                        @ConstructorArg(fieldName ="list") final List<String> list,
                                                                        @ConstructorArg("subObject") final ToSubBean subObject) {
                                                                        this.name = name;
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
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
~~~

### Different field names and types applying transformation through lambda function:

~~~Java
@AllArgsConstructor                                         @AllArgsConstructor
@Getter                                                     @Getter
@Setter                                                     @Setter
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger identifier;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ImmutableToSubFoo> nestedObjectList;                    
   private final String locale;                                private final Locale locale;                    
                                                               private ImmutableToSubFoo nestedObject;
}                                                               
                                                            }
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
@AllArgsConstructor                                         @AllArgsConstructor
@Getter                                                     @Getter
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will be null and no exceptions will be raised
}                                                            }
~~~
And one line code as:
~~~Java
ToBean toBean = beanUtils.getTransformer()
                    .setDefaultValueForMissingField(true).transform(fromBean, ToBean.class);
~~~

### Applying a transformation function in case of missing fields in the source object:

Assign a default value in case of missing field in the source object:

~~~Java
@AllArgsConstructor                                         @AllArgsConstructor
@Getter                                                     @Getter
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
                                                               private final String name;                 
                                                               private String notExistingField; // this will be null and no exceptions will be raised
}                                                            }
~~~
And one line code as:
~~~Java
FieldTransformer<String, String> localeTransformer = new FieldTransformer<>("notExistingField", val -> "sampleVal");
ToBean toBean = beanUtils.getTransformer()
                    .setDefaultValueForMissingField(true).transform(fromBean, ToBean.class);
~~~
       
More sample beans can be found in the test package: `com.hotels.beans.sample`

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
 
## Credits

Created by: [Fabio Borriello](https://github.com/fborriello) with the contribution of: [Patrizio Munzi](https://github.com/patriziomunzi), 
[Andrea Marsiglia](https://github.com/AndreaMars94), [Giorgio Delle Grottaglie](https://github.com/geordie--) & the Hotels.com's Checkout team in Rome.

The application's logo has been designed by: Rob Light.

## Legal

This project is available under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).

Copyright 2018-2019 Expedia Inc.