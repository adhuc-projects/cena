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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;
import org.adhuc.cena.menu.util.ListComparator;

/**
 * An ingredient value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = false, exclude = {"id"}, includeFieldNames = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class IngredientValue extends HateoasClientResourceSupport {

    public static final Comparator<IngredientValue> COMPARATOR = Comparator.comparing(IngredientValue::name);
    public static final Comparator<IngredientValue> NAME_AND_MEASUREMENT_TYPES_COMPARATOR = COMPARATOR
            .thenComparing(IngredientValue::measurementTypes, new ListComparator<>());

    public static final List<String> DEFAULT_MEASUREMENT_TYPES = List.of("WEIGHT", "COUNT");

    private String id;
    private final String name;
    private final List<String> measurementTypes;

    public static IngredientValue buildUnknownIngredientValue(String name, String ingredientsResourceUrl) {
        var ingredient = new IngredientValue(UUID.randomUUID().toString(), name, DEFAULT_MEASUREMENT_TYPES);
        ingredient.addLink(SELF_LINK, String.format("%s/%s", ingredientsResourceUrl, ingredient.id));
        return ingredient;
    }

    public IngredientValue(String name) {
        this(name, DEFAULT_MEASUREMENT_TYPES);
    }

    IngredientValue withoutId() {
        return new IngredientValue(null, name, measurementTypes);
    }

    public IngredientValue withoutMeasurementType() {
        return new IngredientValue(id, name, List.of());
    }

    public IngredientValue withMeasurementTypes(List<String> measurementTypes) {
        return new IngredientValue(id, name, List.copyOf(measurementTypes));
    }

    void assertEqualTo(IngredientValue expected) {
        assertThat(this).usingComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).isEqualTo(expected);
    }
}
