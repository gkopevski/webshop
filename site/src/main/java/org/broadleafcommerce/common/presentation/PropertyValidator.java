/*
 * Copyright 2008-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.broadleafcommerce.common.presentation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.broadleafcommerce.openadmin.dto.BasicFieldMetadata;
import org.broadleafcommerce.openadmin.dto.Entity;
import org.broadleafcommerce.openadmin.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.server.service.persistence.validation.PropertyValidationResult;


/**
 * 
 * @author jfischer
 *
 */

public interface PropertyValidator {

    /**
     * Validates a property for an entity
     *
     * @param entity Entity DTO of the entity attempting to save
     * @param instance actual object representation of <b>entity</b>. This can be cast to entity interfaces (like Sku or
     * Product)
     * @param entityFieldMetadata complete field metadata for all properties in <b>entity</b>
     * @param validationConfiguration the map represented by the set of {@link ConfigurationItem} for a
     * {@link ValidationConfiguration} on a property. This map could be null if this {@link PropertyValidator} is being
     * invoked outside of the context of a particular property (like a global validator)
     * @param propertyMetadata {@link BasicFieldMetadata} corresponding to the property that is being valid
     * @param propertyName the property name of the value attempting to be saved (could be a sub-entity obtained via dot
     * notation like 'defaultSku.name')
     * @param value the value attempted to be saved
     * @return <b>true</b> if this passes validation, <b>false</b> otherwise.
     */
    public PropertyValidationResult validate(Entity entity,
                                            Serializable instance,
                                            Map<String, FieldMetadata> entityFieldMetadata,
                                            Map<String, String> validationConfiguration,
                                            BasicFieldMetadata propertyMetadata,
                                            String propertyName,
                                            String value);
}