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

package com.hotels.map.transformer;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

import static com.hotels.beans.populator.PopulatorFactory.getPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.hotels.beans.populator.Populator;
import com.hotels.beans.populator.PopulatorFactory;
import com.hotels.beans.transformer.BeanTransformer;
import com.hotels.transformer.model.FieldTransformer;
import com.hotels.transformer.utils.ReflectionUtils;

/**
 * Utility methods for populating {@link java.util.Map} elements via reflection.
 */
public class MapTransformerImpl extends AbstractMapTransformer {
    /**
     * {@inheritDoc}
     */
//    @Override
//    public <T, K, R, V> Map<R, V> transform(final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
//        Set<Map.Entry<T, K>> entries = sourceMap.entrySet();
//        Map collect = sourceMap.entrySet()
//                .stream()
//                .collect(
//                        toMap(
//                                e -> {
//                                    T key = e.getKey();
//                                    return getFieldValue(key, beanTransformer, settings.getKeyFieldsTransformers().get(key));
//                                },
//                                e -> getFieldValue()e.getValue()));
//        return null;
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, K> Map<T, K> transform(final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
        return (Map<T, K>) sourceMap.entrySet().stream()
                .collect(
                        toMap(
                                e -> getFieldValue(e.getKey(), beanTransformer, settings.getKeyFieldsTransformers().get(e.getKey())),
                                e -> getFieldValue(e.getValue(), beanTransformer, settings.getFieldsTransformers().get(e.getValue()))
                        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, K, R, V> void transform(final Map<T, K> sourceMap, final Map<R, V> targetMap, final BeanTransformer beanTransformer) {

    }

    private Object getFieldValue(final Object value, final BeanTransformer beanTransformer, final FieldTransformer fieldTransformer) {
        Object newValue;
        Class<?> fieldType = value.getClass();
        if (classUtils.isPrimitiveOrSpecialType(fieldType)) {
            newValue = value;
        } else {
//            newValue = null;
//            Optional<Populator> populator1 = getPopulator(fieldType, fieldType, beanTransformer);
//            Object populatedObject = populator1.get().getPopulatedObject(fieldType, value);
//            System.out.println("populatedObject = " + populatedObject);
            newValue = getPopulator(fieldType, fieldType, beanTransformer)
                    .map(populator -> populator.getPopulatedObject(fieldType, value))
                    .orElseGet(() ->
                            // recursively inject object
                            beanTransformer.transform(value, fieldType)
                    );
        }
        if (nonNull(fieldTransformer)) {
            newValue = fieldTransformer.getTransformedObject(newValue);
        }
        return newValue;
    }

//    private Object getFieldValue(final Object value, final BeanTransformer beanTransformer, final FieldTransformer fieldTransformer) {
//        Object newValue;
//        Class<?> objectClass = value.getClass();
//        Optional<Populator> populator = PopulatorFactory.getPopulator(objectClass, objectClass, beanTransformer);
//        if (Map.class.isAssignableFrom(objectClass)) {
//            newValue = this.transform((Map) value, beanTransformer);
//        } else if (classUtils.isPrimitiveOrSpecialType(objectClass)) {
//            newValue = value;
//        } else {
//           newValue = beanTransformer.transform(value, objectClass);
//        }
//        if (nonNull(fieldTransformer)) {
//            newValue = fieldTransformer.getTransformedObject(newValue);
//        }
//        return newValue;
//    }
}
