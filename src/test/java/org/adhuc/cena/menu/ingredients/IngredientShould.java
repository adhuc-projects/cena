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
package org.adhuc.cena.menu.ingredients;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The {@link Ingredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient should")
class IngredientShould {

    @Test
    @DisplayName("not be creatable without id")
    void notBeCreatableWithoutId() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(null, NAME));
    }

    @Test
    @DisplayName("not be creatable without name")
    void notBeCreatableWithoutName() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(ID, null));
    }

    @Test
    @DisplayName("not be creatable with empty name")
    void notBeCreatableWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(ID, ""));
    }

    @Test
    @DisplayName("contain id and name used during creation")
    void containCreationValues() {
        var ingredient = ingredient();
        assertSoftly(softly -> {
            softly.assertThat(ingredient.id()).isEqualTo(ID);
            softly.assertThat(ingredient.name()).isEqualTo(NAME);
        });
    }

}
