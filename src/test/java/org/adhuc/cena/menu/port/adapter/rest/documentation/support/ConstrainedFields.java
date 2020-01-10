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
import static org.springframework.util.StringUtils.collectionToDelimitedString;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;

/**
 * A supporting class to document constrained fields.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @see ConstraintDescriptions
 * @since 0.1.0
 */
public class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;
    private final List<FieldAlias> aliases;

    /**
     * Creates constrained fields documentation based on the specified constrained class.
     *
     * @param input the constrained class.
     * @param aliases the field aliases to use when a request field's name is not the same as the corresponding request
     *                class' property name.
     */
    public ConstrainedFields(Class<?> input, FieldAlias... aliases) {
        constraintDescriptions = new ConstraintDescriptions(input);
        this.aliases = List.of(aliases);
    }

    /**
     * Gets a field descriptor corresponding to the specified path in the constrained class. The path may have been
     * aliased, then constrained are retrieved for the corresponding property name.
     *
     * @param path the field path.
     * @return a field descriptor with documented constraints.
     */
    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path).attributes(key("constraints").value(
                collectionToDelimitedString(constraintDescriptions.descriptionsForProperty(propertyNameForPath(path)), ". ")));
    }

    private String propertyNameForPath(String path) {
        return aliases.stream().filter(alias -> alias.path.equals(path)).map(alias -> alias.propertyName).findFirst().orElse(path);
    }

    @RequiredArgsConstructor
    public static class FieldAlias {
        @NonNull
        private final String path;
        @NonNull
        private final String propertyName;
    }

}
