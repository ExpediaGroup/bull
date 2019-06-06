<head>
    <title>Samples</title>
</head>

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

From class and To class with different field names:
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
FieldTransformer<String, String> notExistingFieldTransformer = new FieldTransformer<>("notExistingField", () -> "sampleVal");
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
public class ToSubBean {                           
   private final String name;                   
   private final long index;                    
}
~~~
Assuming that the lambda transformation function should be applied only to field: `name` contained into the `ToSubBean` object, the transformation function has to be defined as 
follow:
~~~Java
FieldTransformer<String, String> nameTransformer = new FieldTransformer<>("nestedObject.name", StringUtils::capitalize);
ToBean toBean = beanUtils.getTransformer()
                    .withFieldTransformer(nameTransformer)
                    .transform(fromBean, ToBean.class);
~~~

### Map a primitive type field in the source object into a nested object:

This example shows how to map a primitive field into a nested object into the destination one.

Given:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  private final String name;                   
   private final FromSubBean nestedObject;                     private final ToSubBean nestedObject;                    
   private final int x;
   // all args constructor                                     // all args constructor
   // getters...                                               // getters...
}                                                           }
~~~
and
~~~Java
public class ToSubBean {                           
   private final int x;
   
   // all args constructor
}  // getters...          
~~~
Assuming that the value `x` should be mapped into field: `x` contained into the `ToSubBean` object, the field mapping has to be defined as 
follow:
~~~Java
ToBean toBean = beanUtils.getTransformer()
                    .withFieldMapping(new FieldMapping("x", "nestedObject.x"));
~~~

### Apply a transformation function on all fields matching with the given one:

This example shows how a lambda transformation function can be applied on all fields matching with the given one independently from their position.

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

### Not existing field in the source object:
In case the destination class has a field that does not exist in the source object, but it contains a getter method returning the value, the library gets the field value from that method.
~~~Java
public class FromBean {                                     public class ToBean {                           
                                                               private final BigInteger id;
    public BigInteger getId() {                                   
        return BigInteger.TEN;                                 // all args constructor
   }                                                           // getters...
}                                                               
                                                            }
~~~
And one line code as:
~~~Java
ToBean toBean = beanUtils.getTransformer().transform(fromBean, ToBean.class);
~~~

More sample beans can be found in the test package: `com.hotels.beans.sample` or on DZone: [How to Transform Any Type of Java Bean With BULL](https://dzone.com/articles/how-to-transform-any-type-of-java-bean-with-one-li)