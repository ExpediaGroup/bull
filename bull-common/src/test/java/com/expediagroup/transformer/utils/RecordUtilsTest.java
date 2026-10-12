/**
 * Copyright (C) 2019-2026 Expedia, Inc.
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
package com.expediagroup.transformer.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.RecordComponent;
import java.util.List;

import org.testng.annotations.Test;

import com.expediagroup.beans.sample.immutable.ImmutableToFoo;
import com.expediagroup.beans.sample.record.TestRecord;

/**
 * Unit test for {@link RecordUtils}.
 */
public class RecordUtilsTest {
    /**
     * Tests that getRecordComponents returns components for a record class.
     */
    @Test
    public void testGetRecordComponents() {
        RecordComponent[] components = RecordUtils.getRecordComponents(TestRecord.class);
        assertThat(components).isNotEmpty();
        assertThat(components).hasSize(3);
    }

    /**
     * Tests that getRecordComponents returns empty array for non-record class.
     */
    @Test
    public void testGetRecordComponentsForNonRecord() {
        RecordComponent[] components = RecordUtils.getRecordComponents(ImmutableToFoo.class);
        assertThat(components).isEmpty();
    }

    /**
     * Tests that getRecordComponentNames returns component names.
     */
    @Test
    public void testGetRecordComponentNames() {
        List<String> names = RecordUtils.getRecordComponentNames(TestRecord.class);
        assertThat(names).containsExactly("name", "age", "email");
    }

    /**
     * Tests that getRecordComponentTypes returns component types.
     */
    @Test
    public void testGetRecordComponentTypes() {
        List<Class<?>> types = RecordUtils.getRecordComponentTypes(TestRecord.class);
        assertThat(types).hasSize(3);
        assertThat(types.get(0)).isEqualTo(String.class);
        assertThat(types.get(1)).isEqualTo(int.class);
        assertThat(types.get(2)).isEqualTo(String.class);
    }

    /**
     * Tests that isRecord returns true for record classes.
     */
    @Test
    public void testIsRecord() {
        assertThat(RecordUtils.isRecord(TestRecord.class)).isTrue();
    }

    /**
     * Tests that isRecord returns false for non-record classes.
     */
    @Test
    public void testIsRecordForNonRecord() {
        assertThat(RecordUtils.isRecord(ImmutableToFoo.class)).isFalse();
    }

    /**
     * Tests that getCanonicalConstructor returns the record's canonical constructor.
     */
    @Test
    public void testGetCanonicalConstructor() {
        var constructor = RecordUtils.getCanonicalConstructor(TestRecord.class);
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(3);
    }

    /**
     * Tests that getCanonicalConstructor returns null for non-record classes.
     */
    @Test
    public void testGetCanonicalConstructorForNonRecord() {
        var constructor = RecordUtils.getCanonicalConstructor(ImmutableToFoo.class);
        assertThat(constructor).isNull();
    }

    /**
     * Tests that streamComponents works correctly.
     */
    @Test
    public void testStreamComponents() {
        long count = RecordUtils.streamComponents(TestRecord.class).count();
        assertThat(count).isEqualTo(3);
    }
}
