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
package com.expediagroup.beans.generator.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;

import org.testng.annotations.Test;

import com.expediagroup.beans.generator.core.mapping.MappingCodeFactory;
import com.expediagroup.beans.generator.core.sample.javabean.Destination;
import com.expediagroup.beans.generator.core.sample.javabean.Source;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

/**
 * Test for {@link TransformerSpec}.
 */
public class TransformerSpecTest {
    private static final TypeName I_TRANSFORMER = TypeName.get(Transformer.class);

    /**
     * The class to be tested.
     */
    private final TransformerSpec underTest = new TransformerSpec(MappingCodeFactory.newInstance());

    /**
     * Should be named after source and destination types.
     */
    @Test
    public void shouldBeNamedAfterSourceAndDestinationTypes() {
        // WHEN
        TypeSpec generated = underTest.build(Source.class, Destination.class);

        // THEN
        assertThat(generated.name).as("Transformer name does not include source or destination types")
                .isEqualTo("SourceToDestinationTransformer");
    }

    /**
     * Should implement transformer interface.
     */
    @Test
    public void shouldImplementTransformerInterface() {
        // WHEN
        TypeSpec generated = underTest.build(Source.class, Destination.class);

        // THEN
        assertThat(isATransformer(generated))
                .as("Transformer does not implement interface %s", I_TRANSFORMER)
                .isTrue();
        assertThat(findTransformMethod(generated, Source.class, Destination.class))
                .as("Transformer does not implement method: Destination transform(Source)")
                .isNotNull();
    }

    /**
     * Checks that spec implements Transformer.
     * @param spec a TypeSpec
     * @return true if the spec implements Transformer
     */
    private boolean isATransformer(final TypeSpec spec) {
        return spec.superinterfaces
                .stream()
                .anyMatch(typeName ->
                        typeName instanceof ParameterizedTypeName
                                && ((ParameterizedTypeName) typeName).rawType.equals(I_TRANSFORMER));
    }

    private MethodSpec findTransformMethod(final TypeSpec transformer, final Type sourceClass, final Type destinationClass) {
        return transformer.methodSpecs.stream()
                .filter(methodSpec ->
                        methodSpec.name.equals("transform")
                                && methodSpec.parameters.get(0).type.equals(TypeName.get(sourceClass))
                                && methodSpec.returnType.equals(TypeName.get(destinationClass)))
                .findFirst()
                .orElse(null);
    }
}
