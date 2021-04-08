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
package com.hotels.beans.populator;

import static com.hotels.beans.populator.PopulatorFactory.getPopulator;

import java.lang.reflect.Field;
import java.util.Optional;

import com.hotels.beans.transformer.BeanTransformer;
import com.hotels.transformer.utils.ClassUtils;
import com.hotels.transformer.utils.ReflectionUtils;

/**
 * Populator for collection or map objects.
 * @param <O> the type of the object to get populated.
 */
public abstract class Populator<O> {
    /**
     * Reflection utils instance {@link ReflectionUtils}.
     */
    final ReflectionUtils reflectionUtils;

    /**
     * Class reflection utils instance {@link ClassUtils}.
     */
    final ClassUtils classUtils;

    /**
     * Transformer class instance {@link BeanTransformer} containing the field mapping and transformation functions.
     */
    private final BeanTransformer transformer;

    /**
     * Default constructor.
     * @param beanTransformer the bean transformer containing the field name mapping and transformation functions
     */
    Populator(final BeanTransformer beanTransformer) {
        transformer = beanTransformer;
        reflectionUtils = new ReflectionUtils();
        classUtils = new ClassUtils();
    }

    /**
     * Populates the target object with the values into the source object.
     * @param field the field to be populated
     * @param fieldValue the source object from which extract the values
     * @return a populated list of elements
     */
    protected abstract O getPopulatedObject(Field field, O fieldValue);

    /**
     * Populates the target object with the values into the source object.
     * @param <K> the target object type
     * @param targetClass the destination object class
     * @param fieldName the field to be populated
     * @param fieldValue the source object from which extract the values
     * @return a populated list of elements
     */
    public final <K> O getPopulatedObject(final Class<K> targetClass, final String fieldName, final O fieldValue) {
        return getPopulatedObject(reflectionUtils.getDeclaredField(fieldName, targetClass), fieldValue);
    }

    /**
     * Wrapper method for {@link com.hotels.beans.BeanUtils} transform method.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     */
    public final <T, K> K transform(final T sourceObj, final Class<K> targetClass) {
        return transform(sourceObj, targetClass, null);
    }

    /**
     * Wrapper method for BeanUtil transform method.
     * @param sourceObj the source object
     * @param targetClass the destination object class
     * @param nestedGenericClass the nested generic object class. i.e. in case of object like this:
     *                           {@code List<List<String>>} the value is: String
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a copy of the source object into the destination object
     */
    @SuppressWarnings("unchecked")
    final <T, K> K transform(final T sourceObj, final Class<K> targetClass, final Class<?> nestedGenericClass) {
        final K res;
        if (classUtils.isPrimitiveOrSpecialType(sourceObj.getClass())) {
            res = (K) sourceObj;
        } else {
            final Optional<Populator> optPopulator = getPopulator(targetClass, sourceObj.getClass(), transformer);
            res = (K) optPopulator
                    .map(populator -> ((ICollectionPopulator<Object>) populator).getPopulatedObject(targetClass, targetClass, sourceObj, nestedGenericClass))
                    .orElseGet(() -> transformer.transform(sourceObj, targetClass));
        }
        return res;
    }
}
