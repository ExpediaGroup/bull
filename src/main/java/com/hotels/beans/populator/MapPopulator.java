/**
 * Copyright (C) 2019 Expedia Inc.
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

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Field;
import java.util.Map;

import com.hotels.beans.model.ItemType;
import com.hotels.beans.model.MapElemType;
import com.hotels.beans.model.MapType;
import com.hotels.beans.transformer.Transformer;

/**
 * Populator for Map types object {@link Map}.
 */
class MapPopulator extends Populator<Map<?, ?>> {

    /**
     * Default constructor.
     * @param beanTransformer the bean transformer containing the field name mapping and transformation functions
     */
    MapPopulator(final Transformer beanTransformer) {
        super(beanTransformer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<?, ?> getPopulatedObject(final Field field, final Map<?, ?> fieldValue) {
        final MapType mapGenericType = getReflectionUtils().getMapGenericType(field.getGenericType(), field.getDeclaringClass().getName(), field.getName());
        return getPopulatedObject(fieldValue, mapGenericType, field.getName());
    }

    /**
     * Populates the Map objects.
     * @param fieldValue the source Map.
     * @param mapType the map key, elem types {@link MapType}
     * @param fieldName the name of the field under process
     * @return the populated map
     */
    private Map<?, ?> getPopulatedObject(final Map<?, ?> fieldValue, final MapType mapType, final String fieldName) {
        final MapElemType keyType = mapType.getKeyType();
        final MapElemType elemType = mapType.getElemType();
        final boolean keyIsPrimitive = keyType.getClass().equals(ItemType.class) && getClassUtils().isPrimitiveOrSpecialType(((ItemType) keyType).getObjectClass());
        final boolean elemIsPrimitive = elemType.getClass().equals(ItemType.class) && getClassUtils().isPrimitiveOrSpecialType(((ItemType) elemType).getObjectClass());
        Map<?, ?> populatedObject;
        if (keyIsPrimitive && elemIsPrimitive) {
            populatedObject = fieldValue;
        } else {
            populatedObject = fieldValue.keySet()
                    .stream()
//                    .parallelStream()
                    .collect(toMap(
                        key -> getElemValue(keyType, keyIsPrimitive, key, fieldName),
                        key -> getElemValue(elemType, elemIsPrimitive, fieldValue.get(key), fieldName)));
        }
        return populatedObject;
    }

    /**
     * Gets the value of a given key/elem (including complex types).
     * @param <T> the object type
     * @param mapElemType the map key, elem types {@link MapElemType}
     * @param elemIsPrimitiveType true if the elem map is primitive type, false otherwise.
     * @param value the elem value
     * @param fieldName the name of the field under process
     * @return the element value
     */
    @SuppressWarnings("unchecked")
    private <T> T getElemValue(final MapElemType mapElemType, final boolean elemIsPrimitiveType, final T value, final String fieldName) {
        final T elemValue;
        if (elemIsPrimitiveType || getClassUtils().isPrimitiveOrSpecialType(value.getClass())) {
            elemValue = value;
        } else {
            if (mapElemType.getClass().equals(ItemType.class)) {
                elemValue = (T) transform(value, ((ItemType) mapElemType).getObjectClass(), ((ItemType) mapElemType).getGenericClass(), fieldName);
            } else {
                elemValue = (T) getPopulatedObject((Map) value, (MapType) mapElemType, fieldName);
            }
        }
        return elemValue;
    }
}
