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

import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.hotels.transformer.AbstractTransformerTest;

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
     * Initializes the arguments and objects.
     */
    @BeforeClass
    public void beforeClass() {
        initObjects();
    }

    /**
     * Initialized mocks.
     */
    @BeforeMethod
    public void beforeMethod() {
        initMocks(this);
    }
}
