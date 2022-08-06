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
package com.expediagroup.beans.generator.core.mapping;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.expediagroup.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

/**
 * A specialization of {@link MappingCode} that generates code for mapping between two JavaBeans.
 * The creation code uses the default no-arg constructor.
 * The initialization code uses the available destination setters to map the source getters.
 */
class JavaBeanCode extends MappingCode {
    /**
     * The source type.
     */
    private final Class<?> source;

    /**
     * The destination type.
     */
    private final Class<?> destination;

    /**
     * Instantiates a new JavaBean code model, source and destination types must be JavaBeans.
     * @param sourceBean the source
     * @param destinationBean the destination
     * @param classUtils an utility for introspecting types
     */
    JavaBeanCode(final Class<?> sourceBean, final Class<?> destinationBean, final ClassUtils classUtils) {
        super(classUtils);
        this.source = sourceBean;
        this.destination = destinationBean;
    }

    /**
     * Models the instantiation of destination type with no-arg constructor.
     */
    @Override
    protected CodeBlock creation() {
        return CodeBlock.of("$T destination = new $T()", destination, destination);
    }

    /**
     * Models the mapping from a source type to a mutable destination type.
     */
    @Override
    protected CodeBlock initialization() {
        final CodeBlock.Builder builder = CodeBlock.builder();
        final ClassUtils classUtils = getClassUtils();
        final List<Method> setters = classUtils.getSetterMethods(destination);
        for (Method getter : classUtils.getGetterMethods(source)) {
            findCorrespondingSetter(getter, setters)
                    .ifPresent(setter ->
                            builder.addStatement("destination.$N(source.$N())", setter.getName(), getter.getName()));
        }
        return builder.build();
    }

    /**
     * Searches among {@code setters} for a method corresponding to the same property of a {@code getter}.
     * @param getter a getter in the source type
     * @param setters all the setters in the destination type
     * @return an Optional containing a setter method or empty if not found
     */
    private Optional<Method> findCorrespondingSetter(final Method getter, final Collection<Method> setters) {
        final BeanProperty property = new BeanProperty(getter);
        return setters.stream()
                .filter(s -> new BeanProperty(s).equals(property))
                .findFirst();
    }

}
