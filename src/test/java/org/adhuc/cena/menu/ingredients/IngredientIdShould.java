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

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

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

    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({"invalid"})
    @DisplayName("not be creatable from invalid value")
    void notBeCreatableFromInvalidValue(String value) {
        assertThrows(EntityNotFoundException.class, () -> new IngredientId(value));
    }

    @Test
    @DisplayName("contain identity value used during construction")
    void containCreationValue() {
        var createdId = new IngredientId(ID.toString());
        assertThat(createdId).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when id is equal")
    void isEqual(IngredientId i1, IngredientId i2, boolean expected) {
        assertThat(i1.equals(i2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when id is equal")
    void sameHashCode(IngredientId i1, IngredientId i2, boolean expected) {
        assertThat(i1.hashCode() == i2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        return Stream.of(
                Arguments.of(TOMATO_ID, TOMATO_ID, true),
                Arguments.of(TOMATO_ID, new IngredientId(TOMATO_ID.id()), true),
                Arguments.of(TOMATO_ID, CUCUMBER_ID, false)
        );
    }

}
