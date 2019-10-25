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
