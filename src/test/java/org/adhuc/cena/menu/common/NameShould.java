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
package org.adhuc.cena.menu.common;

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
 * The {@link Name} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Name should")
class NameShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(String value) {
        assertThrows(IllegalArgumentException.class, () -> new Name(value));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of((String) null),
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of(" \t\r\n")
        );
    }

    @ParameterizedTest
    @MethodSource("trimCreationParameters")
    @DisplayName("contain trimmed value after creation")
    void trimValueOnCreation(String creationValue, String expectedValue) {
        var name = new Name(creationValue);
        assertThat(name.value()).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> trimCreationParameters() {
        return Stream.of(
                Arguments.of("Value", "Value"),
                Arguments.of(" Value", "Value"),
                Arguments.of("Value ", "Value"),
                Arguments.of("\t Value \t", "Value"),
                Arguments.of("\r\n\t Value \t\n\r", "Value")
        );
    }

    @ParameterizedTest
    @MethodSource("capitalizeCreationParameters")
    @DisplayName("contain capitalized value after creation")
    void capitalizeOnCreation(String creationValue, String expectedValue) {
        var name = new Name(creationValue);
        assertThat(name.value()).isEqualTo(expectedValue);
    }

    private static Stream<Arguments> capitalizeCreationParameters() {
        return Stream.of(
                Arguments.of("Value", "Value"),
                Arguments.of("value", "Value"),
                Arguments.of(" value", "Value"),
                Arguments.of("value with MULTIPLE words", "Value with MULTIPLE words")
        );
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when value is equal")
    void isEqual(Name n1, Name n2, boolean expected) {
        assertThat(n1.equals(n2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when value is equal")
    void sameHashCode(Name n1, Name n2, boolean expected) {
        assertThat(n1.hashCode() == n2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var name = new Name("Value");
        return Stream.of(
                Arguments.of(name, name, true),
                Arguments.of(name, new Name("Value"), true),
                Arguments.of(name, new Name("Value2"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("equalityIgnoreCaseSource")
    @DisplayName("be equal when value is equal ignoring case")
    void isEqualIgnoreCase(Name n1, Name n2, boolean expected) {
        assertThat(n1.equalsIgnoreCase(n2)).isEqualTo(expected);
    }

    private static Stream<Arguments> equalityIgnoreCaseSource() {
        var name = new Name("Value");
        return Stream.of(
                Arguments.of(name, name, true),
                Arguments.of(name, new Name("Value"), true),
                Arguments.of(name, new Name("VALUE"), true),
                Arguments.of(name, new Name("vaLUe"), true),
                Arguments.of(name, new Name("Value2"), false)
        );
    }

    @Test
    @DisplayName("return value on toString")
    void toStringValue() {
        assertThat(new Name("Value").toString()).isEqualTo("Value");
    }

}
