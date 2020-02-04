/**
 * Copyright (C) 2019-2020 Expedia, Inc.
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

package com.hotels.beans.generator.core.mapping;

import static lombok.AccessLevel.PRIVATE;

import java.util.Objects;

import org.apache.commons.lang3.NotImplementedException;

import com.hotels.transformer.constant.ClassType;
import com.hotels.transformer.utils.ClassUtils;
import com.squareup.javapoet.CodeBlock;

import lombok.RequiredArgsConstructor;

/**
 * A factory to obtain an instance of {@link MappingCode}.
 */
@RequiredArgsConstructor(access = PRIVATE)
public final class MappingCodeFactory {
    /**
     * An instance of ClassUtils.
     */
    private final ClassUtils classUtils;

    /**
     * Gets an instance of {@code MappingCodeFactory}.
     * @return a MappingCode instance
     */
    public static MappingCodeFactory getInstance() {
        return getInstance(new ClassUtils());
    }

    /**
     * Gets an instance of {@code MappingCodeFactory} with the provided classUtils.
     * @param classUtils the class utils
     * @return a MappingCode instance
     */
    public static MappingCodeFactory getInstance(final ClassUtils classUtils) {
        return new MappingCodeFactory(classUtils);
    }

    /**
     * Obtains an instance of {@code MappingCode} from a source and destination.
     * <p>
     * The returned instance is specialized to create a {@link CodeBlock} for mapping
     * an object of a source type to a destination type.
     * @param source the source type
     * @param destination the destination type
     * @return the mapping code from source to destination
     */
    public MappingCode of(final Class<?> source, final Class<?> destination) {
        Objects.requireNonNull(source, "source type can't be null");
        Objects.requireNonNull(destination, "destination type can't be null");

        final ClassType classType = classUtils.getClassType(destination);
        switch (classType) {
            case MUTABLE:
                return new JavaBeanCode(source, destination, classUtils);
            case IMMUTABLE:
                throw new NotImplementedException("Transformation code for type IMMUTABLE is not supported");
            case MIXED:
                throw new NotImplementedException("Transformation code for type MIXED is not supported");
            default:
                throw new IllegalStateException("Should never happen - Unsupported destination type: " + classType);
        }
    }
}
