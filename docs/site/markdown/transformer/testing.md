<head>
    <title>Test your library</title>
</head>

# Overview

Software testing plays a fundamental role in the software development because we can evaluate one or more properties of interest. In general, these properties indicate the 
extent to which the component or system under test:

* meets the requirements that guided its design and development,
* responds correctly to all kinds of inputs,
* performs its functions within an acceptable time,
* it is sufficiently usable,
* can be installed and run in its intended environments
* achieves the general result its stakeholders desire.

This page will show how to test BULL into a simple project. All the examples utilizes [JUnit](https://github.com/junit-team) and [Mockito](https://site.mockito.org/).

The Java Bean transformation function can be tested in two different ways that depends on the following scenarios:

1. The destination object does not require a special configuration to get transformed
2. The destination object requires a special configuration, you are confident that the configuration is working fine as it doesn't includes any special action  
3. The destination object requires a special and complex configuration that needs to be tested as we are not confident that it would work

For both scenarios: 1 and 2 we can use a mocked version the `BeanUtils` object.

### First scenario:

Assuming that our source object and our destination object have been defined as follow:
~~~Java
@AllArgsConstructor                                         @AllArgsConstructor
@Getter                                                     @Getter
@Setter                                                     @Setter
public class SampleRequest {                                public class DestObject {                           
   private final BigInteger x;                                 @NotNull                   
   private final BigInteger y;                                 private BigInteger x;                      
   private final List<FromSubBean> subBeanList;                private final BigInteger y;                 
   private List<String> list;                                  private final List<String> list;                    
   private final FromSubBean subObject;                        private final List<ImmutableToSubFoo> nestedObjectList;                    
   
}                                                           }
~~~
And this is the class containing the `BeanUtils` library:
~~~Java
import ...

public class SampleClass {
    
    private final BeanUtils beanUtils;
    
    public SampleClass() {
        this.beanUtils = new BeanUtils();
    }

    public BigInteger doSomething(final SampleRequest request) {
        final Transformer beanTransformer = beanUtils.getTransformer();
        DestObject destObject = beanTransformer.transform(request, DestObject.class);
        return multiplyValues(destObject.getX(), destObject.getY());
    }
    
    private BigInteger multiplyValues(final BigInteger x, final BigInteger y) {
         return x * y;
    }
}
~~~
The test class will be:
~~~Java
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.transformer.Transformer;

/**
 * Unit test for {@link SampleClass}.
 */
public class SampleClassTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    private SampleClass underTest;
    
    @Mock
    private BeanUtils beanUtils;

    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that a SampleResponse is returned.
     */
    @Test
    public void testDoSomethingWorksProperly() {
        //GIVEN

        //WHEN
        final Transformer transformer = underTest.getTransformer();

        //THEN
        assertNotNull(transformer);
    }
}
~~~
