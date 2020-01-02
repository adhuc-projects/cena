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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link Ingredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient should")
class IngredientShould {

    @Test
    @DisplayName("not be creatable with null creation command")
    void notBeCreatableWithNullCommand() {
        assertThrows(NullPointerException.class, () -> new Ingredient(null));
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, String name, List<QuantityType> quantityTypes) {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(ingredientId, name, quantityTypes));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, QUANTITY_TYPES),
                Arguments.of(ID, null, QUANTITY_TYPES),
                Arguments.of(ID, "", QUANTITY_TYPES),
                Arguments.of(ID, NAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain id, name and quantity types used during creation")
    void containCreationValues(IngredientId ingredientId, String name, List<QuantityType> quantityTypes) {
        var ingredient = new Ingredient(ingredientId, name, quantityTypes);
        assertSoftly(softly -> {
            softly.assertThat(ingredient.id()).isEqualTo(ingredientId);
            softly.assertThat(ingredient.name()).isEqualTo(name);
            softly.assertThat(ingredient.quantityTypes()).isEqualTo(quantityTypes);
        });
    }

    private static Stream<Arguments> validCreationParameters() {
        return Stream.of(
                Arguments.of(ID, NAME, QUANTITY_TYPES),
                Arguments.of(ID, NAME, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when ingredient id is equal")
    void isEqual(Ingredient i1, Ingredient i2, boolean expected) {
        assertThat(i1.equals(i2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id is equal")
    void sameHashCode(Ingredient i1, Ingredient i2, boolean expected) {
        assertThat(i1.hashCode() == i2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var tomato = ingredient(TOMATO_ID, TOMATO, TOMATO_QUANTITY_TYPES);
        return Stream.of(
                Arguments.of(tomato, tomato, true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, TOMATO_QUANTITY_TYPES), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, CUCUMBER, TOMATO_QUANTITY_TYPES), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, List.of()), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, CUCUMBER_QUANTITY_TYPES), true),
                Arguments.of(tomato, ingredient(CUCUMBER_ID, TOMATO, TOMATO_QUANTITY_TYPES), false)
        );
    }

}
