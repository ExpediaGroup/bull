/**
 * Copyright (C) 2019 Expedia, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hotels.beans.transformer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.hotels.beans.model.FieldMapping;
import com.hotels.beans.model.FieldTransformer;
import com.hotels.beans.utils.ReflectionUtils;

/**
 * Unit test for class: {@link Transformer}.
 */
public class TransformerTest extends AbstractTransformerTest {
    private static final String SOURCE_FIELD_NAME = "sourceFieldName";
    private static final String SOURCE_FIELD_NAME_2 = "sourceFieldName2";
    private static final String TRANSFORMER_SETTINGS_FIELD_NAME = "settings";
    private static final String GET_SOURCE_FIELD_VALUE_METHOD_NAME = "getSourceFieldValue";
    private static final ReflectionUtils REFLECTION_UTILS = new ReflectionUtils();

    /**
     * The class to be tested.
     */
    @InjectMocks
    private TransformerImpl underTest;

    /**
     * Initialized mocks.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }

    /**
     * Test that is possible to remove a field mapping for a given field.
     */
    @Test
    public void testRemoveFieldMappingWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.removeFieldMapping(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertFalse(transformerSettings.getFieldsNameMapping().containsKey(DEST_FIELD_NAME));
    }

    /**
     * Test that the method {@code removeFieldMapping} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldMappingRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.removeFieldMapping(null);
    }

    /**
     * Test that is possible to remove all the fields mappings defined.
     */
    @Test
    public void testResetFieldsMappingWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest
                .withFieldMapping(new FieldMapping(SOURCE_FIELD_NAME, DEST_FIELD_NAME), new FieldMapping(SOURCE_FIELD_NAME_2, DEST_FIELD_NAME));

        //WHEN
        beanTransformer.resetFieldsMapping();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertTrue(transformerSettings.getFieldsNameMapping().isEmpty());
    }

    /**
     * Test that is possible to remove all the fields transformer defined.
     */
    @Test
    public void testResetFieldsTransformerWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest
                .withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val), new FieldTransformer<>(REFLECTION_UTILS_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.resetFieldsTransformer();
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertTrue(transformerSettings.getFieldsTransformers().isEmpty());
    }

    /**
     * Test that is possible to remove a field transformer for a given field.
     */
    @Test
    public void testRemoveFieldTransformerWorksProperly() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.removeFieldTransformer(DEST_FIELD_NAME);
        TransformerSettings transformerSettings = (TransformerSettings) REFLECTION_UTILS.getFieldValue(beanTransformer, TRANSFORMER_SETTINGS_FIELD_NAME, TransformerSettings.class);

        //THEN
        assertFalse(transformerSettings.getFieldsTransformers().containsKey(DEST_FIELD_NAME));
    }

    /**
     * Test that the method {@code removeFieldTransformer} raises an {@link IllegalArgumentException} if the parameter is null.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveFieldTransformerRaisesExceptionIfItsCalledWithNullParam() {
        //GIVEN
        Transformer beanTransformer = underTest.withFieldTransformer(new FieldTransformer<>(DEST_FIELD_NAME, val -> val));

        //WHEN
        beanTransformer.removeFieldTransformer(null);
    }

    /**
     * Test that the method: {@code getSourceFieldValue} raises a {@link NullPointerException} in case any of the parameters null.
     */
    @Test(expectedExceptions = Exception.class)
    public void testGetSourceFieldValueRaisesAnExceptionIfTheParameterAreNull() throws Exception {
        //GIVEN
        Method getSourceFieldValueMethod = underTest.getClass().getDeclaredMethod(GET_SOURCE_FIELD_VALUE_METHOD_NAME, Object.class, String.class, Field.class, boolean.class);
        getSourceFieldValueMethod.setAccessible(true);

        //WHEN
        getSourceFieldValueMethod.invoke(underTest, null, null, null, false);
    }
}
