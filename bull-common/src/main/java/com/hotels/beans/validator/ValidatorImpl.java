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

package com.hotels.beans.validator;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import static org.apache.commons.lang3.StringUtils.SPACE;

import static com.hotels.beans.cache.CacheManagerFactory.getCacheManager;
import static com.hotels.beans.constant.Punctuation.DOT;
import static com.hotels.beans.constant.Punctuation.SEMICOLON;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.hotels.beans.error.InvalidBeanException;
import com.hotels.beans.cache.CacheManager;

/**
 * Java Bean validation class.
 * It offers the possibility to validate a given Java Bean against a set of defined constraints.
 */
public class ValidatorImpl implements Validator {
    /**
     * CacheManager class {@link CacheManager}.
     */
    private final CacheManager cacheManager;

    /**
     * Default constructor.
     */
    public ValidatorImpl() {
        this.cacheManager = getCacheManager("validatorImpl");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <K> Set<ConstraintViolation<Object>> getConstraintViolations(final K k) {
        return getValidator().validate(k);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <K> List<String> getConstraintViolationsMessages(final K k) {
        return getConstraintViolations(k).stream()
                .map(cv -> cv.getRootBeanClass().getName()
                        + DOT.getSymbol()
                        + cv.getPropertyPath()
                        + SPACE
                        + cv.getMessage())
                .collect(toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <K> void validate(final K k) {
        final Set<ConstraintViolation<Object>> constraintViolations = getConstraintViolations(k);
        if (!constraintViolations.isEmpty()) {
            final String errors = constraintViolations.stream()
                    .map(cv -> cv.getRootBeanClass().getName()
                            + DOT.getSymbol()
                            + cv.getPropertyPath()
                            + SPACE
                            + cv.getMessage())
                    .collect(joining(SEMICOLON.getSymbol()));
            throw new InvalidBeanException(errors);
        }
    }

    /**
     * Creates the validator.
     * @return a {@link javax.validation.Validator} instance.
     */
    private javax.validation.Validator getValidator() {
        String cacheKey = "BeanValidator";
        return cacheManager.getFromCache(cacheKey, javax.validation.Validator.class)
                .orElseGet(() -> {
                    javax.validation.Validator validator = buildDefaultValidatorFactory().getValidator();
                    cacheManager.cacheObject(cacheKey, validator);
                    return validator;
                });
    }
}
