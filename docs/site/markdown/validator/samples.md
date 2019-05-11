<head>
    <title>Samples</title>
</head>

# Validation samples

Validate a java bean has never been so simple. Given the following bean:

~~~Java
public class SampleBean {                           
   @NotNull                   
   private BigInteger id;                      
   private String name;                 
   
   // constructor
   // getters and setters... 
}                                                               
~~~

an instance of the above object:

~~~Java
SampleBean sampleBean = new SampleBean();
~~~

And one line code as:

~~~Java
beanUtils.getValidator().validate(sampleBean);
~~~

this will throw an `InvalidBeanException` as the `id` field is null.
