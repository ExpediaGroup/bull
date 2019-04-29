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

package com.hotels.beans;

import static com.hotels.beans.utils.ValidationUtils.notNull;

import java.util.function.Function;

import com.hotels.beans.transformer.TransformerImpl;
import com.hotels.beans.transformer.Transformer;

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
     * @param targetClass the destination object class
     * @param <T> the Source object type
     * @param <K> the target object type
     * @return a function that copies of the source object into the destination object
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static <T, K> Function<T, K> getTransformer(final Transformer beanTransformer, final Class<K> targetClass) {
        notNull(beanTransformer, "beanTransformer cannot be null!");
        return fromBean -> beanTransformer.transform(fromBean, targetClass);
    }

    /**
     * Returns a Bean Transformer.
     * @return a Bean Transformer instance.
     */
    public final Transformer getTransformer() {
        return new TransformerImpl();
    }

}
