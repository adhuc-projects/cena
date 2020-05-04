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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.MeasurementUnit.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link Quantity} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Quantity should")
public class QuantityShould {

    @Test
    @DisplayName("have value equal to 1 and unit equal to undefined when undefined")
    void undefined() {
        var quantity = Quantity.UNDEFINED;
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(quantity.value()).isEqualTo(1);
            softAssertions.assertThat(quantity.unit()).isEqualTo(UNDEFINED);
        });
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(int value, MeasurementUnit unit) {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(value, unit));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE, UNIT),
                Arguments.of(0, UNIT),
                Arguments.of(1, null)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain value and measurement unit used during construction")
    void containCreationValue(int value, MeasurementUnit unit) {
        var quantity = new Quantity(value, unit);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(quantity.value()).isEqualTo(value);
            softAssertions.assertThat(quantity.unit()).isEqualTo(unit);
        });
    }

    private static Stream<Arguments> validCreationParameters() {
        return Stream.of(
                Arguments.of(1, DOZEN),
                Arguments.of(10, CENTILITER),
                Arguments.of(Integer.MAX_VALUE, PINCH)
        );
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when value and unit are equal")
    void isEqual(Quantity q1, Quantity q2, boolean expected) {
        assertThat(q1.equals(q2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when value and unit are equal")
    void sameHashCode(Quantity q1, Quantity q2, boolean expected) {
        assertThat(q1.hashCode() == q2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var quantity = new Quantity(10, CENTILITER);
        return Stream.of(
                Arguments.of(quantity, quantity, true),
                Arguments.of(quantity, new Quantity(10, CENTILITER), true),
                Arguments.of(quantity, new Quantity(11, CENTILITER), false),
                Arguments.of(quantity, new Quantity(10, MILLILITER), false),
                Arguments.of(quantity, new Quantity(100, MILLILITER), false)
        );
    }

}
