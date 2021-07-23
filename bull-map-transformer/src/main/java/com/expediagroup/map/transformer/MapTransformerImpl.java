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
package com.expediagroup.map.transformer;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

import static com.expediagroup.beans.populator.PopulatorFactory.getPopulator;
import static com.expediagroup.transformer.validator.Validator.notNull;

import java.util.HashMap;
import java.util.Map;

import com.expediagroup.beans.transformer.BeanTransformer;
import com.expediagroup.transformer.error.InvalidFunctionException;
import com.expediagroup.transformer.model.FieldTransformer;

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
        validateParameters(sourceMap, beanTransformer);
        Map<?, FieldTransformer> keyFieldsTransformers = settings.getKeyFieldsTransformers();
        Map<T, K> res;
        if (settings.getFieldsNameMapping().isEmpty() && keyFieldsTransformers.isEmpty()) {
            res = new HashMap<>(sourceMap);
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
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, K, R, V> Map<R, V> transform(final Map<T, K> sourceMap, final BeanTransformer beanTransformer, final Class<R> targetKeyType, final Class<V> targetElemType) {
        validateParameters(sourceMap, beanTransformer);
        Map<?, FieldTransformer> keyFieldsTransformers = settings.getKeyFieldsTransformers();
        return (Map<R, V>) sourceMap.entrySet().stream()
                    .collect(toMap(
                            e -> getTransformedObject(keyFieldsTransformers.get(e.getKey()), e.getKey(), beanTransformer, targetKeyType),
                            e -> getTransformedObject(settings.getFieldsTransformers().get(e.getKey()), getMapValue(e, sourceMap), beanTransformer, targetElemType)
                    ));
    }

    /**
     * Checks that the input parameter are valid.
     * @param sourceMap the Map to transform
     * @param beanTransformer the {@link BeanTransformer} instance
     * @param <T> the key object type in the source map
     * @param <K> the elem object type in the source map
     */
    private <T, K> void validateParameters(final Map<T, K> sourceMap, final BeanTransformer beanTransformer) {
        notNull(sourceMap, "The map to copy cannot be null!");
        notNull(beanTransformer, "The bean transformer to use cannot be null!");
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
     * Applies the {@link FieldTransformer} function (if any) to the given Map element value.
     * @param fieldTransformer the {@link FieldTransformer} function to apply
     * @param value the object on which the function has to be applied
     * @param beanTransformer the bean transformer to use for the map elements transformation
     * @param targetClass the destination object class
     * @param <K> the return class type
     * @return the transformed value
     */
    @SuppressWarnings("unchecked")
    private <K> K getTransformedObject(final FieldTransformer<Object, Object> fieldTransformer, final Object value,
        final BeanTransformer beanTransformer, final Class<K> targetClass) {
        K newValue;
        if (Map.class.isAssignableFrom(value.getClass())) {
            newValue = (K) transform((Map) value, beanTransformer);
        } else {
            newValue = (K) getPopulator(targetClass, value.getClass(), beanTransformer)
                .map(populator -> populator.transform(value, targetClass))
                .orElseGet(() -> beanTransformer.transform(value, targetClass));
        }
        return (K) getTransformedObject(fieldTransformer, newValue);
    }

    /**
     * Applies the {@link FieldTransformer} function (if any) to the given Map element value.
     * @param fieldTransformer the {@link FieldTransformer} function to apply
     * @param value the object on which the function has to be applied
     * @return the transformed value
     * @throws InvalidFunctionException if the defined function is not valid
     */
    private Object getTransformedObject(final FieldTransformer<Object, Object> fieldTransformer, final Object value) {
        try {
            return nonNull(fieldTransformer) ? fieldTransformer.getTransformedObject(value) : value;
        } catch (final Exception e) {
            throw new InvalidFunctionException("The transformer function defined for the map key is not valid.", e);
        }
    }
}
