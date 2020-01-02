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
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;

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

    public static final Comparator<IngredientValue> COMPARATOR = new IngredientNameComparator();

    private String id;
    private final String name;

    public static IngredientValue buildUnknownIngredientValue(String name, String ingredientsResourceUrl) {
        var ingredient = new IngredientValue(UUID.randomUUID().toString(), name);
        ingredient.addLink(SELF_LINK, String.format("%s/%s", ingredientsResourceUrl, ingredient.id));
        return ingredient;
    }

    IngredientValue withoutId() {
        return new IngredientValue(null, name);
    }

    void assertEqualTo(IngredientValue expected) {
        assertThat(this).usingComparator(COMPARATOR).isEqualTo(expected);
    }

    static class IngredientNameComparator implements Comparator<IngredientValue> {
        @Override
        public int compare(IngredientValue ingredient1, IngredientValue ingredient2) {
            return Comparator.comparing(IngredientValue::name, String::compareTo).compare(ingredient1, ingredient2);
        }
    }

}
