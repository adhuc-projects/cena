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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.ID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.adhuc.cena.menu.common.EntityNotFoundException;

/**
 * The {@link IngredientId} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient identity should")
class IngredientIdShould {

    @Test
    @DisplayName("not be creatable from null value")
    void notBeCreatableFromNullValue() {
        assertThrows(EntityNotFoundException.class, () -> new IngredientId(null));
    }

    @Test
    @DisplayName("not be creatable from invalid value")
    void notBeCreatableFromInvalidString() {
        assertThrows(EntityNotFoundException.class, () -> new IngredientId("invalid"));
    }

    @Test
    @DisplayName("contain identity value used during construction")
    void containCreationValue() {
        var createdId = new IngredientId(ID.toString());
        assertThat(createdId).isEqualTo(ID);
    }

}
