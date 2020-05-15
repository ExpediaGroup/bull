<head>
    <title>Samples</title>
</head>

# Validation samples

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
