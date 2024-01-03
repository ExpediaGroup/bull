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

import static java.util.Arrays.stream;

import java.lang.reflect.Field;

import com.expediagroup.beans.transformer.BeanTransformer;

/**
 * Populator for primitive types array.
 */
class ArrayPopulator extends Populator<Object> implements ICollectionPopulator<Object> {

    /**
     * Default constructor.
     * @param beanTransformer the bean transformer containing the field name mapping and transformation functions
     */
    ArrayPopulator(final BeanTransformer beanTransformer) {
        super(beanTransformer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPopulatedObject(final Field field, final Object fieldValue) {
        return getPopulatedObject(field.getType(), reflectionUtils.getArrayType(field), fieldValue, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPopulatedObject(final Class<?> fieldType, final Class<?> genericFieldType, final Object fieldValue, final Class<?> nestedGenericClass) {
        final Object res;
        if (classUtils.isPrimitiveTypeArray(fieldValue.getClass()) || classUtils.isPrimitiveOrSpecialType(genericFieldType)) {
            res = fieldValue;
        } else {
            res = stream((Object[]) fieldValue)
                    //.parallel()
                    .map(o -> classUtils.isPrimitiveOrSpecialType(o.getClass()) ? o : transform(o, genericFieldType)).toArray();
        }
        return res;
    }
}
