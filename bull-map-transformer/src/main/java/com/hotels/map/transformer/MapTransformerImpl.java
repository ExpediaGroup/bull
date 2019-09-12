/**
 * Copyright (C) 2019 Expedia, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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
import static com.hotels.transformer.validator.Validator.notNull;

import java.util.Map;

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
    @Override
    @SuppressWarnings("unchecked")
    public <T, K> Map<T, K> transform(final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
        notNull(sourceMap, "The map to copy cannot be null!");
        notNull(beanTransformer, "The bean transformer to use cannot be null!");
        Map<?, FieldTransformer> keyFieldsTransformers = settings.getKeyFieldsTransformers();
        Map<T, K> res;
        if (settings.getFieldsNameMapping().isEmpty() && keyFieldsTransformers.isEmpty()) {
            res = Map.copyOf(sourceMap);
        } else {
            res = (Map<T, K>) sourceMap.entrySet().stream()
                    .collect(toMap(
                            e -> getTransformedObject(keyFieldsTransformers.get(e.getKey()), e.getKey()),
                            e -> getTransformedObject(settings.getFieldsTransformers().get(e.getKey()), getMapValue(e, sourceMap))
                    ));
        }
        return res;
    }

    /**
     * Checks if a mapping has been defined between one key and the other.
     * In case a mapping exists, it returns the Map value for the new key.
     * @param entry the Map entry from which extract the key and the value.
     * @param sourceMap the Map from which the data has to be retrieved.
     * @param <T> the key type
     * @param <K> the class type
     * @return the value for the new key
     */
    @SuppressWarnings("unchecked")
    private <T, K> K getMapValue(final Map.Entry<T, K> entry, final Map<T, K> sourceMap) {
        return sourceMap.getOrDefault((T) settings.getFieldsNameMapping().get(entry.getKey()), entry.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T, K, R, V> Map<R, V> transform(final Map<T, K> sourceMap, final Class<R> targetKeyClass, final Class<V> targetElemClass, final BeanTransformer beanTransformer) {
        notNull(sourceMap, "The source map to copy cannot be null!");
        notNull(targetKeyClass, "The target key class cannot be null!");
        notNull(targetElemClass, "The target element class cannot be null!");
        notNull(beanTransformer, "The bean transformer to use cannot be null!");
        return (Map<R, V>) sourceMap.entrySet().stream()
                .collect(toMap(
                    e -> getFieldValue(e.getKey(), targetKeyClass, settings.getKeyFieldsTransformers().get(e.getKey()), beanTransformer),
                    e -> getFieldValue(getMapValue(e, sourceMap), targetElemClass, settings.getFieldsTransformers().get(e.getKey()), beanTransformer)
                ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, K, R, V> void transform(final Map<T, K> sourceMap, final Map<R, V> targetMap, final BeanTransformer beanTransformer) {
        notNull(sourceMap, "The source map to copy cannot be null!");
        notNull(targetMap, "The target map cannot be null!");
        notNull(beanTransformer, "The bean transformer to use cannot be null!");
    }

    private Object getFieldValue(final Object value, final Class<?> targetClass, final FieldTransformer fieldTransformer, final BeanTransformer beanTransformer) {
        Object newValue;
        Class<?> fieldType = value.getClass();
        if (classUtils.isPrimitiveOrSpecialType(fieldType)) {
            newValue = value;
        } else {
            newValue = getPopulator(fieldType, fieldType, beanTransformer)
                    .map(populator -> populator.getPopulatedObject(fieldType, value))
                    .orElseGet(() -> beanTransformer.transform(value, targetClass));
        }
        newValue = getTransformedObject(fieldTransformer, newValue);
        return newValue;
    }

    private Object getTransformedObject(final FieldTransformer<Object, Object> fieldTransformer, final Object value) {
        Object newValue = value;
        if (nonNull(fieldTransformer)) {
            newValue = fieldTransformer.getTransformedObject(value);
        }
        return newValue;
    }
}
