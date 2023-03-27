/**
 * Copyright (C) 2018-2023 Expedia, Inc.
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
package com.expediagroup.beans;

import static com.expediagroup.transformer.validator.Validator.notNull;

import java.util.function.Function;

import com.expediagroup.beans.conversion.Converter;
import com.expediagroup.beans.conversion.ConverterImpl;
import com.expediagroup.beans.transformer.BeanTransformer;
import com.expediagroup.beans.transformer.TransformerImpl;
import com.expediagroup.transformer.validator.Validator;
import com.expediagroup.transformer.validator.ValidatorImpl;

/**
 * Set of Bean utilities.
 */
public class BeanUtils {

    /**
     * Returns a function that transforms an object T in an object K.
     * @param targetClass the destination object class
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a function that copies of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static <T, K> Function<T, K> getTransformer(final Class<K> targetClass) {
        return fromBean -> new TransformerImpl().transform(fromBean, targetClass);
    }

    /**
     * Returns a function that transforms an object T in an object K.
     * @param beanTransformer the transformer to be used.
     * @param targetClass the destination object
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a function that copies of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static <T, K> Function<T, K> getTransformer(final BeanTransformer beanTransformer, final Class<K> targetClass) {
        notNull(beanTransformer, "beanTransformer cannot be null!");
        return fromBean -> beanTransformer.transform(fromBean, targetClass);
    }

    /**
     * Returns a Bean Transformer.
     * @return a {@link BeanTransformer} instance.
     */
    public final BeanTransformer getTransformer() {
        return new TransformerImpl();
    }

    /**
     * Returns a Bean Validator.
     * @return a Bean Validator instance.
     */
    public final Validator getValidator() {
        return new ValidatorImpl();
    }

    /**
     * Returns a primitive type converter.
     * @return a {@link Converter} instance.
     */
    public final Converter getPrimitiveTypeConverter() {
        return new ConverterImpl();
    }

}
