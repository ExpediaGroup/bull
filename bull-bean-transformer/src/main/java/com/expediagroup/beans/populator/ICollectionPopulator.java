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

/**
 * Interface class containing the methods needed for the collection related object types.
 * @param <O> the class of the collection elements.
 */
interface ICollectionPopulator<O> {
    /**
     * Populates the array of the target object (contained into the object generics) with the values into the source object.
     * @param fieldType the field type
     * @param genericFieldType the field to be populated class
     * @param fieldValue the source object from which extract the values
     * @param nestedGenericClass the nested generic object class. i.e. in case of object like this: {@code List<List<String>>} the value is: String
     * @return a populated list of elements
     */
    O getPopulatedObject(Class<?> fieldType, Class<?> genericFieldType, Object fieldValue, Class<?> nestedGenericClass);
}
