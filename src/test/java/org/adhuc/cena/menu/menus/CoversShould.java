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
package org.adhuc.cena.menu.menus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link Covers} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Covers should")
class CoversShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(int value) {
        assertThrows(IllegalArgumentException.class, () -> new Covers(value));
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
        var covers = new Covers(value);
        assertThat(covers.value()).isEqualTo(value);
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
    void isEqual(Covers c1, Covers c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when value is equal")
    void sameHashCode(Covers c1, Covers c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var covers = new Covers(6);
        return Stream.of(
                Arguments.of(covers, covers, true),
                Arguments.of(covers, new Covers(6), true),
                Arguments.of(covers, new Covers(1), false),
                Arguments.of(covers, new Covers(5), false),
                Arguments.of(covers, new Covers(Integer.MAX_VALUE), false)
        );
    }

}
