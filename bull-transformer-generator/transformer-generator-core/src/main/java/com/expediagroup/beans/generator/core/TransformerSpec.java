/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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

import static javax.lang.model.element.Modifier.PUBLIC;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import com.expediagroup.beans.generator.core.mapping.MappingCodeFactory;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import lombok.RequiredArgsConstructor;

/**
 * This class is used to build a {@link Transformer} implementation model to map between a source and destination types.
 * The model is a {@link TypeSpec} object that can be further modified if needed, and serialized to source code.
 */
@RequiredArgsConstructor
public class TransformerSpec {
    /**
     * A reusable reference to {@link Transformer#transform(Object)} method.
     */
    private static final Method TRANSFORM = Transformer.class.getMethods()[0];

    /**
     * A {@link MappingCodeFactory} to create code blocks.
     */
    private final MappingCodeFactory codeFactory;

    /**
     * Build a transformer model to map from a {@code source} to {@code destination} type.
     * The generated model implements the {@link Transformer} interface and provides mapping code
     * to instantiate a destination object and map values from the source object.
     * @param <A> the source type
     * @param <B> the destination type
     * @param source the source to read values from
     * @param destination the destination where to map values
     * @return a transformer model
     */
    public <A, B> TypeSpec build(final Class<A> source, final Class<B> destination) {
        return TypeSpec.classBuilder(nameWith(source, destination))
                .addModifiers(PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(Transformer.class, source, destination))
                .addMethod(overrideTransform(source, destination)
                        .addCode(codeFactory.of(source, destination).build())
                        .build())
                .build();
    }

    /**
     * Creates a name for the transformer implementation that includes {@code source} and {@code destination}.
     * @param source the source type
     * @param destination the destination type
     * @return a transformer name
     */
    private String nameWith(final Class<?> source, final Class<?> destination) {
        return MessageFormat.format("{0}To{1}Transformer", source.getSimpleName(), destination.getSimpleName());
    }

    /**
     * Creates a {@link MethodSpec.Builder} configured with the declaration to override
     * the {@link Transformer#transform(Object)} method.
     * @param source the source type
     * @param destination the destination type
     * @return a method builder with overridden transform() declaration
     */
    private MethodSpec.Builder overrideTransform(final Class<?> source, final Class<?> destination) {
        return MethodSpec.methodBuilder(TRANSFORM.getName())
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(source, "source")
                .returns(destination);
    }
}
