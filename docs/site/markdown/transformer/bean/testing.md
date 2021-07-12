<head>
    <title>Test your library</title>
</head>

# Overview

Software testing plays a fundamental role in software development because we can evaluate one or more properties of interest. In general, these properties indicate the 
the extent to which the component or system under test:

* Meets the requirements that guided its design and development,
* responds correctly to all kinds of inputs,
* performs its functions within an acceptable time,
* it is sufficiently usable,
* Can be installed and run in its intended environments
* achieves the general result of its stakeholder's desire.

This page will show how to test BULL into a simple project. All the examples utilize [JUnit](https://github.com/junit-team), [Mockito](https://site.mockito.org/) and [AssertJ](https://assertj.github.io/doc/)

The Java Bean transformation function can be tested in two different ways that depend on the following scenarios:

1. The destination object does not require a special configuration to get transformed
2. The destination object requires a special configuration, but you are confident that the configuration is working fine as it doesn't include any special action  
3. The destination object requires a special configuration that needs to be tested as we are not confident that it would work

For both scenarios: 1 and 2 we can use a mocked version of the `BeanUtils` object.

### Before start

As BULL contains final methods that need to be mocked and, as Mockito requires a special configuration in order to mock final classes/methods, an extension needs
to be added to the test resource folder. This file is available [here](https://github.com/ExpediaGroup/bull/tree/master/src/test/resources/mockito-extensions).

All the examples will be based on the following source object and destination object:

```java
public class SampleRequest {                                public class DestObject {                           
   private final BigInteger x;                                 @NotNull                   
   private final BigInteger y;                                 private BigInteger x;                      
                                                               private final BigInteger y;                 
   // constructors                                             // constructors
   // getters and setters                                      // getters and setters
}                                                           }
```

### First scenario

#####The destination object does not require a special configuration to get transformed

Given the following service class that contains the `BeanUtils` library:
```java
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
         return x.multiply(y);
    }
}
```
The test class will be:
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.transformer.BeanUtils;
import com.hotels.transformer.Transformer;
import com.hotels.transformer.error.InvalidBeanException;

/**
 * Unit test for {@link SampleClass}.
 */
public class SampleClassTest {
    private static final BigInteger EXPECTED_RESULT = new BigInteger(20); 
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
        openMocks(this);
    }

    /**
     * Test that a SampleResponse is returned.
     */
    @Test
    public void testDoSomethingWorksProperly() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(BigInteger.TEN, BigInteger.TWO);
        DestObject destObject = createDestObject(BigInteger.TEN, BigInteger.TWO);
        Transformer beanTransformer = mock(Transformer.class);
        when(beanUtils.getTransformer()).thenReturn(beanTransformer);
        when(beanTransformer.transform(sampleRequest, DestObject.class)).thenReturn(destObject);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
        verify(beanUtils).getTransformer();
        verify(beanTransformer).transform(sampleRequest, DestObject.class);
        assertThat(actual).isEqualTo(EXPECTED_RESULT);
    }
    
    /**
     * Test that an {@link InvalidBeanException} is thrown when the {@code id} field in the {@link SampleRequest}
     * doesn't met the constraints defined into {@link DestObject}.
     */
    @Test(expected = InvalidBeanException.class)
    public void testThatAnExceptionIsThrownWhenConstraintsAreNotMet() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(null, BigInteger.TWO);
        Transformer beanTransformer = mock(Transformer.class);
        when(beanUtils.getTransformer()).thenReturn(beanTransformer);
        when(beanTransformer.transform(sampleRequest, DestObject.class)).thenThrow(InvalidBeanException.class);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
    }
    
    private SampleRequest createSampleRequest(final BigInteger x, final BigInteger y) {
        return new SampleRequest();
    }
    
    private DestObject createDestObject(final BigInteger x, final BigInteger y) {
       return new DestObject(x, y);
    }
}
```

### Second scenario:

#####The destination object requires a special configuration, but you are confident that the configuration is working fine as it doesn't include any particular instruction.

Given the following service class:
```java
import ...

public class SampleClass {
    
    private final BeanUtils beanUtils;
    
    public SampleClass() {
        this.beanUtils = new BeanUtils();
    }

    public BigInteger doSomething(final SampleRequest request) {
        final Transformer beanTransformer = beanUtils.getTransformer();
        FieldTransformer<BigInteger, BigInteger> prodTransformer = new FieldTransformer<>("y", val -> val.multiply(BigInteger.TWO));
        DestObject destObject = beanTransformer
                                   .withFieldTransformer(prodTransformer)
                                   .transform(request, DestObject.class);
        return multiplyValues(destObject.getX(), destObject.getY());
    }
    
    private BigInteger multiplyValues(final BigInteger x, final BigInteger y) {
         return x.multiply(y);
    }
}
```
The test class will be:
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.transformer.BeanUtils;
import com.hotels.transformer.Transformer;
import com.hotels.transformer.error.InvalidBeanException;

/**
 * Unit test for {@link SampleClass}.
 */
public class SampleClassTest {
    private static final BigInteger EXPECTED_RESULT = new BigInteger(40); 
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
        openMocks(this);
    }

    /**
     * Test that a SampleResponse is returned.
     */
    @Test
    public void testDoSomethingWorksProperly() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(BigInteger.TEN, BigInteger.TWO);
        DestObject destObject = createDestObject(BigInteger.TEN, BigInteger.TWO.multiply(BigInteger.TWO));
        Transformer beanTransformer = mock(Transformer.class);
        when(beanUtils.getTransformer()).thenReturn(beanTransformer);
        when(beanTransformer.withFieldTransformer(any(FieldTransformer.class))).thenReturn(beanTransformer);
        when(beanTransformer.transform(sampleRequest, DestObject.class)).thenReturn(destObject);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
        verify(beanUtils).getTransformer();
        verify(beanTransformer).withFieldTransformer(any(FieldTransformer.class));
        verify(beanTransformer).transform(sampleRequest, DestObject.class);
        assertThat(actual).isEqualTo(EXPECTED_RESULT);
    }
    
    /**
     * Test that an {@link InvalidBeanException} is thrown when the {@code id} field in the {@link SampleRequest}
     * doesn't met the constraints defined into {@link DestObject}.
     */
    @Test(expected = InvalidBeanException.class)
    public void testThatAnExceptionIsThrownWhenConstraintsAreNotMet() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(null, BigInteger.TWO);
        Transformer beanTransformer = mock(Transformer.class);
        when(beanUtils.getTransformer()).thenReturn(beanTransformer);
        when(beanTransformer.withFieldTransformer(any(FieldTransformer.class))).thenReturn(beanTransformer);
        when(beanTransformer.transform(sampleRequest, DestObject.class)).thenThrow(InvalidBeanException.class);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
    }
    
    private SampleRequest createSampleRequest(final BigInteger x, final BigInteger y) {
        return new SampleRequest();
    }
    
    private DestObject createDestObject(final BigInteger x, final BigInteger y) {
        return new DestObject(x, y);
    }
}
```

### Third scenario:

#####The destination object requires a special configuration that needs to be tested as you are not confident that it would work

Given the following service class:
```java
import ...

public class SampleClass {
    
    private final BeanUtils beanUtils;
    
    public SampleClass() {
        this.beanUtils = new BeanUtils();
    }

    public BigInteger doSomething(final SampleRequest request) {
        final Transformer beanTransformer = beanUtils.getTransformer();
        FieldTransformer<BigInteger, BigInteger> prodTransformer = new FieldTransformer<>("y", val -> val.multiply(BigInteger.TWO));
        DestObject destObject = beanTransformer
                                   .withFieldTransformer(prodTransformer)
                                   .transform(request, DestObject.class);
        return multiplyValues(destObject.getX(), destObject.getY());
    }
    
    private BigInteger multiplyValues(final BigInteger x, final BigInteger y) {
         return x.multiply(y);
    }
}
```
The test class will be:
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import com.hotels.beans.transformer.BeanUtils;

/**
 * Unit test for {@link SampleClass}.
 */
public class SampleClassTest {
    private static final BigInteger EXPECTED_RESULT = new BigInteger(40); 
    /**
     * The class to be tested.
     */
    @InjectMocks
    private SampleClass underTest;
    
    /**
     * Initialized mocks.
     */
    @Before
    public void beforeMethod() {
        openMocks(this);
        // injects a real BeanUtils instance into the test class
        setFieldValue(underTest, "beanUtils", new BeanUtils());
    }

    /**
     * Test that a SampleResponse is returned.
     */
    @Test
    public void testDoSomethingWorksProperly() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(BigInteger.TEN, BigInteger.TWO);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
        assertThat(actual).isEqualTo(EXPECTED_RESULT);
    }
    
    /**
     * Test that an {@link InvalidBeanException} is thrown when the {@code id} field in the {@link SampleRequest}
     * doesn't met the constraints defined into {@link DestObject}.
     */
    @Test(expected = InvalidBeanException.class)
    public void testThatAnExceptionIsThrownWhenConstraintsAreNotMet() {
        //GIVEN
        SampleRequest sampleRequest = createSampleRequest(null, BigInteger.TWO);
        
        //WHEN
        final BigInteger actual = underTest.doSomething(sampleRequest);

        //THEN
    }
    
    private SampleRequest createSampleRequest(final BigInteger x, final BigInteger y) {
        return new SampleRequest();
    }
    
    /**
     * Sets the value of a field through {@link Field#set} method.
     * @param target the field's class
     * @param fieldName the field's name to set
     * @param fieldValue the value to set
     */
    private void setFieldValue(final Object target, final String fieldName, final Object fieldValue) throws NoSuchFieldException {
        final Field field = target.getClass().getDeclaredField(fieldName);
        final boolean isAccessible = field.canAccess(target);
        try {
            if (!isAccessible) {
                field.setAccessible(true);
            }
            field.set(target, fieldValue);
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }
}
```
