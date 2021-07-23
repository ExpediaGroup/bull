/**
 * Copyright (C) 2019-2021 Expedia, Inc.
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

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.expediagroup.beans.sample.record.FromFooRecord;
import com.expediagroup.beans.sample.record.RecordToFoo;

/**
 * Unit test for all {@link BeanTransformer} functions related to a Java record.
 */
public class RecordObjectTransformationTest extends AbstractBeanTransformerTest {
    /**
     * Test that the transformation between Records works properly.
     */
    @Test
    public void testRecordIsCorrectlyCopied() {
        // GIVEN
        var sourceObject = new FromFooRecord(ID, NAME);

        // WHEN
        var actual = underTest.transform(sourceObject, RecordToFoo.class);

        // THEN
        assertThat(actual).usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(sourceObject);
    }

    /**
     * Test that the transformation from a bean to a records works properly.
     */
    @Test
    public void testThatARecordIsCorrectlyCopiedFromANormalBean() {
        // GIVEN

        // WHEN
        var actual = underTest.transform(fromFoo, RecordToFoo.class);

        // THEN
        assertThat(actual)
                .extracting(ID_FIELD_NAME, NAME_FIELD_NAME)
                .containsExactly(fromFoo.getId(), fromFoo.getName());
    }

}
