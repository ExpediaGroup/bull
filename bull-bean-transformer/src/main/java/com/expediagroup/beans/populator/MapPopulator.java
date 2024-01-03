/**
 * Copyright (C) 2019-2023 Expedia, Inc.
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
package com.expediagroup.beans.populator;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Map;

import com.expediagroup.beans.transformer.BeanTransformer;
import com.expediagroup.transformer.model.ItemType;
import com.expediagroup.transformer.model.MapElemType;
import com.expediagroup.transformer.model.MapType;

/**
 * Populator for Map types object {@link Map}.
 */
class MapPopulator extends Populator<Map<?, ?>> {

    /**
     * Default constructor.
     * @param beanTransformer the bean transformer containing the field name mapping and transformation functions
     */
    MapPopulator(final BeanTransformer beanTransformer) {
        super(beanTransformer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<?, ?> getPopulatedObject(final Field field, final Map<?, ?> fieldValue) {
        final MapType mapGenericType = reflectionUtils.getMapGenericType(field.getGenericType(), field.getDeclaringClass().getName(), field.getName());
        return getPopulatedObject(fieldValue, mapGenericType);
    }

    /**
     * Populates the Map objects.
     * @param fieldValue the source Map.
     * @param mapType the map key, elem types {@link MapType}
     * @return the populated map
     */
    private Map<?, ?> getPopulatedObject(final Map<?, ?> fieldValue, final MapType mapType) {
        final MapElemType keyType = mapType.getKeyType();
        final MapElemType elemType = mapType.getElemType();
        final boolean keyIsPrimitive = isPrimitive(keyType);
        final boolean elemIsPrimitive = isPrimitive(elemType);
        Map<?, ?> populatedObject;
        if (keyIsPrimitive && elemIsPrimitive) {
            populatedObject = fieldValue;
        } else {
            populatedObject = fieldValue.keySet()
                    .stream()
//                    .parallelStream()
                    .collect(toMap(
                        key -> getElemValue(keyType, keyIsPrimitive, key),
                        key -> getElemValue(elemType, elemIsPrimitive, fieldValue.get(key))));
        }
        return populatedObject;
    }

    /**
     * Checks if the map element type is primitive or not.
     * @param mapElemType the map element to check
     * @return true if it's primitive, false otherwise
     */
    private boolean isPrimitive(final MapElemType mapElemType) {
        return mapElemType.getClass().equals(ItemType.class) && classUtils.isPrimitiveOrSpecialType(((ItemType) mapElemType).getObjectClass());
    }

    /**
     * Gets the value of a given key/elem (including complex types).
     * @param <T> the object type
     * @param mapElemType the map key, elem types {@link MapElemType}
     * @param elemIsPrimitiveType true if the elem map is primitive type, false otherwise.
     * @param value the elem value
     * @return the element value
     */
    @SuppressWarnings("unchecked")
    private <T> T getElemValue(final MapElemType mapElemType, final boolean elemIsPrimitiveType, final T value) {
        final T elemValue;
        if (elemIsPrimitiveType || classUtils.isPrimitiveOrSpecialType(value.getClass())) {
            elemValue = value;
        } else {
            if (mapElemType.getClass().equals(ItemType.class)) {
                elemValue = (T) transform(value, ((ItemType) mapElemType).getObjectClass(), ((ItemType) mapElemType).getGenericClass());
            } else {
                elemValue = (T) getPopulatedObject((Map) value, (MapType) mapElemType);
            }
        }
        return elemValue;
    }
}
