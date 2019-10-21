/*
 * Copyright (C) 2019 Alexandre Carbenay
 *
 * This file is part of Cena Project.
 *
 * Cena Project is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Cena Project is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Cena Project. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.adhuc.cena.menu.port.adapter.rest.documentation.support;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.util.StringUtils;

/**
 * A supporting class to document constrained fields.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @see ConstraintDescriptions
 * @since 0.1.0
 */
public class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    /**
     * Creates constrained fields documentation based on the specified constrained class.
     *
     * @param input the constrained class.
     */
    public ConstrainedFields(Class<?> input) {
        constraintDescriptions = new ConstraintDescriptions(input);
    }

    /**
     * Gets a field descriptor corresponding to the specified path in the constrained class.
     *
     * @param path the field path.
     * @return a field descriptor with documented constraints.
     */
    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path).attributes(key("constraints").value(
                StringUtils.collectionToDelimitedString(constraintDescriptions.descriptionsForProperty(path), ". ")));
    }

}
