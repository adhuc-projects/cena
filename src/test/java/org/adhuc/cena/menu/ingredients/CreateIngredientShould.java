/*
 * Copyright (C) 2019-2020 Alexandre Carbenay
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.common.Name;

/**
 * The {@link CreateIngredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient creation command should")
class CreateIngredientShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, Name name, List<MeasurementType> measurementTypes) {
        assertThrows(IllegalArgumentException.class, () -> new CreateIngredient(ingredientId, name, measurementTypes));
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
    @DisplayName("contain id, name and measurement types used during creation")
    void containCreationValues(IngredientId ingredientId, Name name, List<MeasurementType> measurementTypes) {
        var command = new CreateIngredient(ingredientId, name, measurementTypes);
        assertSoftly(softly -> {
            softly.assertThat(command.ingredientId()).isEqualTo(ingredientId);
            softly.assertThat(command.ingredientName()).isEqualTo(name);
            softly.assertThat(command.ingredientMeasurementTypes()).isEqualTo(measurementTypes);
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
    @DisplayName("be equal when ingredient id and name are equal")
    void isEqual(CreateIngredient c1, CreateIngredient c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id and name are equal")
    void sameHashCode(CreateIngredient c1, CreateIngredient c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var createTomato = new CreateIngredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES);
        return Stream.of(
                Arguments.of(createTomato, createTomato, true),
                Arguments.of(createTomato, new CreateIngredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES), true),
                Arguments.of(createTomato, new CreateIngredient(CUCUMBER_ID, TOMATO, TOMATO_MEASUREMENT_TYPES), false),
                Arguments.of(createTomato, new CreateIngredient(TOMATO_ID, CUCUMBER, TOMATO_MEASUREMENT_TYPES), false),
                Arguments.of(createTomato, new CreateIngredient(TOMATO_ID, CUCUMBER, List.of()), false),
                Arguments.of(createTomato, new CreateIngredient(TOMATO_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES), false)
        );
    }

}
