package com.hotels.beans.validator;

import com.hotels.beans.error.InvalidBeanException;

/**
 * Java Bean validation class.
 * It offers the possibility to validate a given Java Bean against a set of defined constraints.
 */
public interface Validator {
    /**
     * Checks if an object is valid.
     * @param k the object to check
     * @param <K> the object class
     * @throws InvalidBeanException {@link InvalidBeanException} if the validation fails
     */
    <K> void validate(final K k);
}
