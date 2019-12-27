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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import org.adhuc.cena.menu.common.EntityNotFoundException;

/**
 * The {@link RecipeId} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe identity should")
class RecipeIdShould {

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({"invalid"})
    @DisplayName("not be creatable from invalid value")
    void notBeCreatableFromInvalidValue(String value) {
        assertThrows(EntityNotFoundException.class, () -> new RecipeId(value));
    }

    @Test
    @DisplayName("contain identity value used during construction")
    void containCreationValue() {
        var createdId = new RecipeId(ID.id());
        assertThat(createdId).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when id is equal")
    void isEqual(RecipeId r1, RecipeId r2, boolean expected) {
        assertThat(r1.equals(r2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when id is equal")
    void sameHashCode(RecipeId r1, RecipeId r2, boolean expected) {
        assertThat(r1.hashCode() == r2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        return Stream.of(
                Arguments.of(TOMATO_CUCUMBER_MOZZA_SALAD_ID, TOMATO_CUCUMBER_MOZZA_SALAD_ID, true),
                Arguments.of(TOMATO_CUCUMBER_MOZZA_SALAD_ID, new RecipeId(TOMATO_CUCUMBER_MOZZA_SALAD_ID.id()), true),
                Arguments.of(TOMATO_CUCUMBER_MOZZA_SALAD_ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, false)
        );
    }

}
