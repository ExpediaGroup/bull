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

package com.hotels.beans.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Specifies the field's name mapping between the source object and destination one.
 * @deprecated This class will be removed since version 1.6.0 please use {@link com.hotels.transformer.model.FieldMapping} instead.
 */
@Deprecated(since = "1.6.0", forRemoval = true)
@Getter
@ToString
public class FieldMapping extends com.hotels.transformer.model.FieldMapping {
    /**
     * @param sourceFieldName The field name in the source object.
     * @param destFieldName The field name in the destination object.
     */
    public FieldMapping(final String sourceFieldName, final String destFieldName) {
        super(sourceFieldName, destFieldName);
    }
}
