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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Tests that getRecordComponentNames returns empty for non-record.
     */
    @Test
    public void testGetRecordComponentNamesForNonRecord() {
        List<String> names = RecordUtils.getRecordComponentNames(ImmutableToFoo.class);
        assertThat(names).isEmpty();
    }

    /**
     * Tests that getRecordComponentTypes returns empty for non-record.
     */
    @Test
    public void testGetRecordComponentTypesForNonRecord() {
        List<Class<?>> types = RecordUtils.getRecordComponentTypes(ImmutableToFoo.class);
        assertThat(types).isEmpty();
    }

    /**
     * Tests that streamComponents works for non-record.
     */
    @Test
    public void testStreamComponentsForNonRecord() {
        long count = RecordUtils.streamComponents(ImmutableToFoo.class).count();
        assertThat(count).isEqualTo(0);
    }

    /**
     * Tests that record components names match expected.
     */
    @Test
    public void testRecordComponentNamesMatch() {
        List<String> names = RecordUtils.getRecordComponentNames(TestRecord.class);
        assertThat(names).containsExactly("name", "age", "email");
        assertThat(names.get(0)).isEqualTo("name");
        assertThat(names.get(1)).isEqualTo("age");
        assertThat(names.get(2)).isEqualTo("email");
    }

    /**
     * Tests that record component types are correct classes.
     */
    @Test
    public void testRecordComponentTypesAreCorrect() {
        List<Class<?>> types = RecordUtils.getRecordComponentTypes(TestRecord.class);
        assertThat(types.get(0)).isEqualTo(String.class);
        assertThat(types.get(1).isPrimitive()).isTrue();
        assertThat(types.get(2)).isEqualTo(String.class);
    }

    /**
     * Tests that streamComponents collects all components.
     */
    @Test
    public void testStreamComponentsCollect() {
        List<RecordComponent> components = RecordUtils.streamComponents(TestRecord.class).collect(Collectors.toList());
        assertThat(components).hasSize(3);
        assertThat(components.get(0).getName()).isEqualTo("name");
        assertThat(components.get(1).getName()).isEqualTo("age");
        assertThat(components.get(2).getName()).isEqualTo("email");
    }

    /**
     * Tests that getCanonicalConstructor is not null for record.
     */
    @Test
    public void testGetCanonicalConstructorNotNull() {
        var constructor = RecordUtils.getCanonicalConstructor(TestRecord.class);
        assertThat(constructor).isNotNull();
        assertThat(constructor.getParameterCount()).isEqualTo(3);
    }

    /**
     * Tests that canonical constructor has correct parameter count.
     */
    @Test
    public void testCanonicalConstructorParameterCount() {
        var constructor = RecordUtils.getCanonicalConstructor(TestRecord.class);
        assertThat(constructor.getParameterCount()).isEqualTo(3);
    }

    /**
     * Tests that getRecordComponents has correct size.
     */
    @Test
    public void testRecordComponentsSize() {
        RecordComponent[] components = RecordUtils.getRecordComponents(TestRecord.class);
        assertThat(components.length).isEqualTo(3);
    }

    /**
     * Tests that isRecord correctly identifies records.
     */
    @Test
    public void testIsRecordIdentification() {
        assertThat(RecordUtils.isRecord(TestRecord.class)).isTrue();
        assertThat(RecordUtils.isRecord(ImmutableToFoo.class)).isFalse();
    }

    /**
     * Tests that non-record types return empty components.
     */
    @Test
    public void testNonRecordEmptyComponents() {
        RecordComponent[] components = RecordUtils.getRecordComponents(ImmutableToFoo.class);
        assertThat(components).isEmpty();
        assertThat(components.length).isEqualTo(0);
    }

    /**
     * Tests that stream operations work on record components.
     */
    @Test
    public void testStreamOperationsOnRecord() {
        long nameCount = RecordUtils.streamComponents(TestRecord.class).filter(rc -> rc.getName().equals("name"))
            .count();
        assertThat(nameCount).isEqualTo(1);
    }

    /**
     * Tests that getRecordComponentNames preserves order.
     */
    @Test
    public void testComponentNamesPreserveOrder() {
        List<String> names = RecordUtils.getRecordComponentNames(TestRecord.class);
        assertThat(names).containsExactly("name", "age", "email");
        assertThat(names).isSortedAccordingTo((a, b) -> {
            if (a.equals("name")) return -1;
            if (a.equals("age")) return -1;
            return 1;
        });
    }

    /**
     * Tests that component names are retrieved correctly for each position.
     */
    @Test
    public void testRecordComponentIndividualNames() {
        RecordComponent[] components = RecordUtils.getRecordComponents(TestRecord.class);
        assertThat(components[0].getName()).isEqualTo("name");
        assertThat(components[1].getName()).isEqualTo("age");
        assertThat(components[2].getName()).isEqualTo("email");
    }

    /**
     * Tests that component types are individual correct classes.
     */
    @Test
    public void testRecordComponentIndividualTypes() {
        RecordComponent[] components = RecordUtils.getRecordComponents(TestRecord.class);
        assertThat(components[0].getType()).isEqualTo(String.class);
        assertThat(components[1].getType()).isEqualTo(int.class);
        assertThat(components[2].getType()).isEqualTo(String.class);
    }

    /**
     * Tests that all static methods are accessible.
     */
    @Test
    public void testStaticMethodsAccessible() {
        assertThat(RecordUtils.isRecord(TestRecord.class)).isNotNull();
        assertThat(RecordUtils.getRecordComponents(TestRecord.class)).isNotNull();
    }

    /**
     * Tests that canonical constructor can be invoked to create instance.
     */
    @Test
    public void testCanonicalConstructorCanCreateInstance() throws Exception {
        var constructor = RecordUtils.getCanonicalConstructor(TestRecord.class);
        assertThat(constructor).isNotNull();
        var instance = constructor.newInstance("John", 30, "john@example.com");
        assertThat(instance).isInstanceOf(TestRecord.class);
    }

    /**
     * Tests that canonical constructor parameter types match component types.
     */
    @Test
    public void testCanonicalConstructorParameterTypesMatchComponents() {
        var constructor = RecordUtils.getCanonicalConstructor(TestRecord.class);
        var parameterTypes = constructor.getParameterTypes();
        var componentTypes = RecordUtils.getRecordComponentTypes(TestRecord.class);
        assertThat(parameterTypes.length).isEqualTo(componentTypes.size());
        for (int i = 0; i < parameterTypes.length; i++) {
            assertThat(parameterTypes[i]).isEqualTo(componentTypes.get(i));
        }
    }

    /**
     * Tests getRecordComponentNames and Types work together.
     */
    @Test
    public void testComponentNamesAndTypesCorrespond() {
        List<String> names = RecordUtils.getRecordComponentNames(TestRecord.class);
        List<Class<?>> types = RecordUtils.getRecordComponentTypes(TestRecord.class);
        assertThat(names).hasSize(types.size());
        assertThat(names.size()).isEqualTo(3);
        assertThat(types.size()).isEqualTo(3);
    }
}
