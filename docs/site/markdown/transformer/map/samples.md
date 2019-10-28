<head>
    <title>Samples</title>
</head>

# `Map` Transformation samples

### `Map` clone:

Given a simple `Map` defined as follow:

~~~Java
Map<String, List<String>> map = new HashMap<>();
map.put("key", List.of("value1", "value2"));
~~~

it can be cloned using the following command:

~~~Java
Map<String, List<String>> newMap = new MapUtils().getTransformer().transform(map);
~~~

### Map a `Key` value into a different `Key` in the destination `Map`:

Given a simple `Map` defined as follow:

~~~Java
Map<String, String> map = new HashMap<>();
map.put("key1", "Hello");
map.put("key2", "Dude");
~~~

And assuming that we want that the `key2` value, in the destination map, has to be the `key1` one,
the only thing we need to do is to define the field mapping:

~~~Java
FieldMapping<String, String> keyMapping = new FieldMapping<>("key1", "key2");
Map<String, List<String>> newMap = new MapUtils().getTransformer()
                                        .withFieldMapping(keyMapping)
                                        .transform(map);
~~~

Then if we run the following command:

~~~Java
System.out.println(newMap.get("key2"));
~~~

the output will be:

~~~Java
Hello
~~~

### Apply a transformation function on a `Map` key:

Given a simple `Map` defined as follow:

~~~Java
Map<String, String> map = new HashMap<>();
map.put("name", "John");
map.put("surname", "Smith");
~~~

And assuming that we want that the `name` correspondent key is made upper case,
the only thing we need to do is to apply a `FieldTransformer` to the `key` defined as following:

~~~Java
FieldTransformer<String, String> keyTransformer = new FieldTransformer<>("name", String::toUpperCase);
Map<String, List<String>> newMap = new MapUtils().getTransformer()
                                        .withKeyTransformer(keyTransformer)
                                        .transform(map);
~~~

Then the key: "name" in the `newMap`will be: `NAME`

### Apply a transformation function on a `Map` value:

Given a simple `Map` defined as follow:

~~~Java
Map<String, Integer> map = new HashMap<>();
map.put("key1", 30);
map.put("key2", 200);
~~~

And assuming that we want that the `key1` correspondent value is raised to the power of 2,
the only thing we need to do is to define a `FieldTransformer` as following:

~~~Java
FieldTransformer<Integer, Integer> keyPow = new FieldTransformer<>("key1", val -> Math.pow(val, 2));
Map<String, List<String>> newMap = new MapUtils().getTransformer()
                                        .withFieldTransformer(keyPow)
                                        .transform(map);
~~~

Then if we run the following command:

~~~Java
System.out.println(newMap.get("key1"));
~~~

the output will be:

~~~Java
900
~~~

### Transform `Map` `key` or `value` object into a different object

Assuming that we have a map defined as follow:

~~~Java
Map<FromBean, FromSubBean> sourceMap = new HashMap<>();
~~~

and we want to transform it in:

~~~Java
Map<ToBean, ToSubBean> map = new HashMap<>();
~~~

where `FromBean` and `ToBean` are:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String name;                                  @NotNull                   
   private final BigInteger id;                                public BigInteger id;                      
   private final List<FromSubBean> subBeanList;                private final String name;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ToSubBean> subBeanList;                    
                                                               private ToSubBean subObject;
   
   // all constructors                                         // all args constructor
   // getters and setters...                                   // getters and setters... 
}    
~~~

and `FromSubBean` and `ToSubBean` are:

~~~Java
public class FromBean {                                     public class ToBean {                           
   private final String index;                                 private final String index;
   
   // all constructor                                         // all args constructor
   // getters...                                              // getters... 
}    
~~~

what we need to do is to specify the Map `Key` and `Element` class in the destination Map.

In our case it will be:

~~~Java
Map<ToBean, ToSubBean> map = new MapUtils().getTransformer()
                                           .transform(sourceMap, ToBean.class, ToSubBean.class);
~~~

### Configure a `BeanTransformer` and use it fo the `Map` transformation

Assuming that we have two classes with different field names defined as follow:

~~~Java
public class FromBean {                                     public class ToBean {                           
                                                                                       
   private final String name;                                  private final String differentName;                   
   private final int id;                                       private final int id;                      
   private final List<FromSubBean> subBeanList;                private final List<ToSubBean> subBeanList;                 
   private final List<String> list;                            private final List<String> list;                    
   private final FromSubBean subObject;                        private final ToSubBean subObject;                    
    
   // all constructor                                         // all args constructor
   // getters...                                              // getters... 
}
~~~

and our source `Map` is:

~~~Java
Map<FromBean, List<String>> sourceMap = new HashMap<>();
~~~

and we want to transform it in:

~~~Java
Map<ToBean, List<String>> map = new HashMap<>();
~~~

we first need to define a `BeanTransformer` that maps the different field names:
~~~Java                                                                
BeanTransformer beanTransformer = new BeanUtils().getTransformer()
                                                 .withFieldMapping(new FieldMapping<>("name", "differentName"));                                                              
~~~

and then pass it as argument of our Map transformation method:

~~~Java
Map<ToBean, List<String>> map = new MapUtils().getTransformer()
                                           .transform(sourceMap, beanTransformer, ToBean.class, List.class);
~~~