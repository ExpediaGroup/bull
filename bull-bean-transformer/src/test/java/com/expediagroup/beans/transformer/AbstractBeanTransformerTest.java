/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.beans.transformer;

import static org.mockito.MockitoAnnotations.openMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.expediagroup.transformer.AbstractTransformerTest;
import com.expediagroup.transformer.utils.ReflectionUtils;

/**
 * Unit test for {@link BeanTransformer}.
 */
public abstract class AbstractBeanTransformerTest extends AbstractTransformerTest {
    /**
     * The class to be tested.
     */
    @InjectMocks
    TransformerImpl underTest;

    /**
     * The reflection utils class.
     */
    final ReflectionUtils reflectionUtils = new ReflectionUtils();

    /**
     * Initializes the arguments and objects.
     */
    @BeforeClass
    void beforeClass() {
        initObjects();
    }

    /**
     * Initialized mocks.
     */
    @BeforeMethod
    void beforeMethod() {
        openMocks(this);
        underTest.resetFieldsTransformer();
        underTest.resetFieldsTransformationSkip();
    }
}
