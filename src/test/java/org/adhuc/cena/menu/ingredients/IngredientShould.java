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

import org.adhuc.cena.menu.common.Name;

/**
 * The {@link Ingredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
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
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, Name name, List<MeasurementType> measurementTypes) {
        assertThrows(IllegalArgumentException.class, () -> new Ingredient(ingredientId, name, measurementTypes));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, MEASUREMENT_TYPES),
                Arguments.of(ID, null, MEASUREMENT_TYPES),
                Arguments.of(ID, NAME, null)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain id, name and measurement types from command used during creation")
    void containCreationValuesFromCommand(IngredientId ingredientId, Name name, List<MeasurementType> measurementTypes) {
        var command = new CreateIngredient(ingredientId, name, measurementTypes);
        var ingredient = new Ingredient(command);
        assertSoftly(softly -> {
            softly.assertThat(ingredient.id()).isEqualTo(ingredientId);
            softly.assertThat(ingredient.name()).isEqualTo(name);
            softly.assertThat(ingredient.measurementTypes()).isEqualTo(measurementTypes);
        });
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain id, name and measurement types used during creation")
    void containCreationValues(IngredientId ingredientId, Name name, List<MeasurementType> measurementTypes) {
        var ingredient = new Ingredient(ingredientId, name, measurementTypes);
        assertSoftly(softly -> {
            softly.assertThat(ingredient.id()).isEqualTo(ingredientId);
            softly.assertThat(ingredient.name()).isEqualTo(name);
            softly.assertThat(ingredient.measurementTypes()).isEqualTo(measurementTypes);
        });
    }

    private static Stream<Arguments> validCreationParameters() {
        return Stream.of(
                Arguments.of(ID, NAME, MEASUREMENT_TYPES),
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
        var tomato = ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES);
        return Stream.of(
                Arguments.of(tomato, tomato, true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, CUCUMBER, TOMATO_MEASUREMENT_TYPES), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, List.of()), true),
                Arguments.of(tomato, ingredient(TOMATO_ID, TOMATO, CUCUMBER_MEASUREMENT_TYPES), true),
                Arguments.of(tomato, ingredient(CUCUMBER_ID, TOMATO, TOMATO_MEASUREMENT_TYPES), false)
        );
    }

}
