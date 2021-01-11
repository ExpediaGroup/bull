<head>
    <title>Samples</title>
</head>

# Converter Samples

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

**IMPORTANT:** The primitive type transformation (if enabled) is executed before any other `FieldTransformer` function defined on a specific field.
This means that once the `FieldTransformer` function will be executed the field value has already been transformed.
