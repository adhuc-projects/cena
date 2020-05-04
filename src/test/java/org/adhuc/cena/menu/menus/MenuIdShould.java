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
package org.adhuc.cena.menu.menus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.MenuMother.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link MenuId} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menu identity should")
class MenuIdShould {

    @ParameterizedTest
    @MethodSource("invalidCreationValuesSource")
    @DisplayName("not be creatable from invalid values")
    void notBeCreatableFromInvalidValue(MenuOwner owner, LocalDate date, MealType mealType) {
        assertThrows(IllegalArgumentException.class, () -> new MenuId(owner, date, mealType));
    }

    private static Stream<Arguments> invalidCreationValuesSource() {
        return Stream.of(
                Arguments.of(null, DATE, MEAL_TYPE),
                Arguments.of(OWNER, null, MEAL_TYPE),
                Arguments.of(OWNER, DATE, null)
        );
    }

    @Test
    @DisplayName("contain values used during construction")
    void containCreationValues() {
        var createdId = new MenuId(ID.owner(), ID.date(), ID.mealType());
        assertThat(createdId).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when values are equal")
    void isEqual(MenuId m1, MenuId m2, boolean expected) {
        assertThat(m1.equals(m2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when values are equal")
    void sameHashCode(MenuId m1, MenuId m2, boolean expected) {
        assertThat(m1.hashCode() == m2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var todayLunchId = new MenuId(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE, TODAY_LUNCH_MEAL_TYPE);
        return Stream.of(
                Arguments.of(todayLunchId, todayLunchId, true),
                Arguments.of(todayLunchId, new MenuId(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE, TODAY_LUNCH_MEAL_TYPE), true),
                Arguments.of(todayLunchId, new MenuId(new MenuOwner("other owner"), TODAY_LUNCH_DATE, TODAY_LUNCH_MEAL_TYPE), false),
                Arguments.of(todayLunchId, new MenuId(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE.plusDays(1), TODAY_LUNCH_MEAL_TYPE), false),
                Arguments.of(todayLunchId, new MenuId(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE, MealType.DINNER), false)
        );
    }

}
