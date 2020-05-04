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

import static org.adhuc.cena.menu.ingredients.MeasurementType.*;
import static org.adhuc.cena.menu.recipes.MeasurementUnit.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.ingredients.MeasurementType;

/**
 * The {@link MeasurementUnit} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Measurement unit should")
class MeasurementUnitShould {

    @Test
    @DisplayName("throw IllegalArgumentException when checking association with null measurement types")
    void throwIAEIsAssociatedToNullMeasurementTypes() {
        assertThrows(IllegalArgumentException.class, () -> UNDEFINED.isAssociatedToOneOf((Collection<MeasurementType>) null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when checking association with empty measurement types collection")
    void throwIAEIsAssociatedToEmptyMeasurementTypesCollection() {
        assertThrows(IllegalArgumentException.class, () -> UNDEFINED.isAssociatedToOneOf(List.of()));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when checking association with empty measurement types")
    void throwIAEIsAssociatedToEmptyMeasurementTypes() {
        assertThrows(IllegalArgumentException.class, () -> UNDEFINED.isAssociatedToOneOf());
    }

    @Test
    @DisplayName("throw IllegalArgumentException when checking association with null measurement type")
    void throwIAEIsAssociatedToNullMeasurementType() {
        assertThrows(IllegalArgumentException.class, () -> UNDEFINED.isAssociatedToOneOf(WEIGHT, null, VOLUME));
    }

    @ParameterizedTest
    @MethodSource("singleMeasurementTypeParameters")
    @DisplayName("be associated to single measurement type")
    void beAssociatedToSingleMeasurementType(MeasurementUnit unit, MeasurementType type, boolean associated) {
        assertThat(unit.isAssociatedToOneOf(type)).isEqualTo(associated);
    }

    private static Stream<Arguments> singleMeasurementTypeParameters() {
        return Stream.of(
                Arguments.of(UNDEFINED, WEIGHT, true),
                Arguments.of(UNDEFINED, VOLUME, true),
                Arguments.of(UNDEFINED, COUNT, true),
                Arguments.of(UNDEFINED, AT_CONVENIENCE, true),
                Arguments.of(LITER, WEIGHT, false),
                Arguments.of(LITER, VOLUME, true),
                Arguments.of(LITER, COUNT, false),
                Arguments.of(LITER, AT_CONVENIENCE, false),
                Arguments.of(CENTILITER, WEIGHT, false),
                Arguments.of(CENTILITER, VOLUME, true),
                Arguments.of(CENTILITER, COUNT, false),
                Arguments.of(CENTILITER, AT_CONVENIENCE, false),
                Arguments.of(MILLILITER, WEIGHT, false),
                Arguments.of(MILLILITER, VOLUME, true),
                Arguments.of(MILLILITER, COUNT, false),
                Arguments.of(MILLILITER, AT_CONVENIENCE, false),
                Arguments.of(TABLESPOON, WEIGHT, false),
                Arguments.of(TABLESPOON, VOLUME, true),
                Arguments.of(TABLESPOON, COUNT, false),
                Arguments.of(TABLESPOON, AT_CONVENIENCE, false),
                Arguments.of(TEASPOON, WEIGHT, false),
                Arguments.of(TEASPOON, VOLUME, true),
                Arguments.of(TEASPOON, COUNT, false),
                Arguments.of(TEASPOON, AT_CONVENIENCE, false),
                Arguments.of(GRAM, WEIGHT, true),
                Arguments.of(GRAM, VOLUME, false),
                Arguments.of(GRAM, COUNT, false),
                Arguments.of(GRAM, AT_CONVENIENCE, false),
                Arguments.of(KILOGRAM, WEIGHT, true),
                Arguments.of(KILOGRAM, VOLUME, false),
                Arguments.of(KILOGRAM, COUNT, false),
                Arguments.of(KILOGRAM, AT_CONVENIENCE, false),
                Arguments.of(POUND, WEIGHT, true),
                Arguments.of(POUND, VOLUME, false),
                Arguments.of(POUND, COUNT, false),
                Arguments.of(POUND, AT_CONVENIENCE, false),
                Arguments.of(UNIT, WEIGHT, false),
                Arguments.of(UNIT, VOLUME, false),
                Arguments.of(UNIT, COUNT, true),
                Arguments.of(UNIT, AT_CONVENIENCE, false),
                Arguments.of(TEN, WEIGHT, false),
                Arguments.of(TEN, VOLUME, false),
                Arguments.of(TEN, COUNT, true),
                Arguments.of(TEN, AT_CONVENIENCE, false),
                Arguments.of(DOZEN, WEIGHT, false),
                Arguments.of(DOZEN, VOLUME, false),
                Arguments.of(DOZEN, COUNT, true),
                Arguments.of(DOZEN, AT_CONVENIENCE, false),
                Arguments.of(PINCH, WEIGHT, false),
                Arguments.of(PINCH, VOLUME, false),
                Arguments.of(PINCH, COUNT, false),
                Arguments.of(PINCH, AT_CONVENIENCE, true)
        );
    }

}
