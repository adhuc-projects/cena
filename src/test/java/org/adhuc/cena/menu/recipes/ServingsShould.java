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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link Servings} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Servings should")
class ServingsShould {

    @Test
    @DisplayName("have default value equal to 4")
    void defaultValue() {
        var servings = Servings.DEFAULT;
        assertThat(servings.value()).isEqualTo(4);
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(int value) {
        assertThrows(IllegalArgumentException.class, () -> new Servings(value));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE),
                Arguments.of(-1),
                Arguments.of(0)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain value used during construction")
    void containCreationValue(int value) {
        var servings = new Servings(value);
        assertThat(servings.value()).isEqualTo(value);
    }

    private static Stream<Arguments> validCreationParameters() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(10),
                Arguments.of(Integer.MAX_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when value is equal")
    void isEqual(Servings s1, Servings s2, boolean expected) {
        assertThat(s1.equals(s2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when value is equal")
    void sameHashCode(Servings s1, Servings s2, boolean expected) {
        assertThat(s1.hashCode() == s2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var servings = new Servings(6);
        return Stream.of(
                Arguments.of(servings, servings, true),
                Arguments.of(servings, new Servings(6), true),
                Arguments.of(servings, new Servings(1), false),
                Arguments.of(servings, new Servings(5), false),
                Arguments.of(servings, new Servings(Integer.MAX_VALUE), false)
        );
    }

}
