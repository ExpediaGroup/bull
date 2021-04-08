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

import static java.util.Optional.ofNullable;

import java.lang.reflect.Field;
import java.util.Optional;

import com.hotels.beans.transformer.BeanTransformer;

/**
 * Populator for {@link Optional} type.
 */
class OptionalPopulator extends Populator<Object> {

    /**
     * Default constructor.
     * @param beanTransformer the bean transformer containing the field name mapping and transformation functions
     */
    OptionalPopulator(final BeanTransformer beanTransformer) {
        super(beanTransformer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPopulatedObject(final Field field, final Object fieldValue) {
        Object res = null;
        Optional optionalFieldValue = (Optional) fieldValue;
        if (optionalFieldValue.isPresent()) {
            res = transform(optionalFieldValue.get(), field.getType());
        }
        // if the field type in the target class is not an optional it puts the value inside an optional
        if (field.getType() == Optional.class) {
            res = ofNullable(res);
        }
        return res;
    }
}
