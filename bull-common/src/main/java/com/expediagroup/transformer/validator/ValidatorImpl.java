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
package com.expediagroup.transformer.validator;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

import static org.apache.commons.lang3.StringUtils.SPACE;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import jakarta.validation.ConstraintViolation;

import com.expediagroup.transformer.cache.CacheManager;
import com.expediagroup.transformer.cache.CacheManagerFactory;
import com.expediagroup.transformer.constant.Punctuation;
import com.expediagroup.transformer.error.InvalidBeanException;

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
        this.cacheManager = CacheManagerFactory.getCacheManager("validatorImpl");
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
                .map(getConstraintViolationMessage())
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
                    .map(getConstraintViolationMessage())
                    .collect(joining(Punctuation.SEMICOLON.getSymbol()));
            throw new InvalidBeanException(errors);
        }
    }

    /**
     * Creates a function that, given a constraint error, builds a violation message.
     * @return the constant violation message
     */
    private Function<ConstraintViolation<Object>, String> getConstraintViolationMessage() {
        return cv -> cv.getRootBeanClass().getName()
                + Punctuation.DOT.getSymbol()
                + cv.getPropertyPath()
                + SPACE
                + cv.getMessage();
    }

    /**
     * Creates the validator.
     * @return a {@link jakarta.validation.Validator} instance.
     */
    private jakarta.validation.Validator getValidator() {
        var cacheKey = "BeanValidator";
        return cacheManager.getFromCache(cacheKey, jakarta.validation.Validator.class)
                .orElseGet(() -> {
                    try (var validatorFactory = buildDefaultValidatorFactory()) {
                        var validator = validatorFactory.getValidator();
                        cacheManager.cacheObject(cacheKey, validator);
                        return validator;
                    }
                });
    }
}
