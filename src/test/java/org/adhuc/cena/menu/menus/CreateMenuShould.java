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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.MenuMother.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * The {@link CreateMenu} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menu creation command should")
public class CreateMenuShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(MenuOwner owner, LocalDate date, MealType mealType, Covers covers, Set<RecipeId> mainCourseRecipes) {
        assertThrows(IllegalArgumentException.class, () -> new CreateMenu(owner, date, mealType, covers, mainCourseRecipes));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, DATE, MEAL_TYPE, COVERS, MAIN_COURSE_RECIPES),
                Arguments.of(OWNER, null, MEAL_TYPE, COVERS, MAIN_COURSE_RECIPES),
                Arguments.of(OWNER, DATE, null, COVERS, MAIN_COURSE_RECIPES),
                Arguments.of(OWNER, DATE, MEAL_TYPE, null, MAIN_COURSE_RECIPES),
                Arguments.of(OWNER, DATE, MEAL_TYPE, COVERS, null),
                Arguments.of(OWNER, DATE, MEAL_TYPE, COVERS, Collections.emptySet())
        );
    }

    @Test
    @DisplayName("contain values used during creation")
    void containCreationValues() {
        var command = createCommand();
        assertSoftly(softly -> {
            softly.assertThat(command.menuId()).isEqualTo(ID);
            softly.assertThat(command.covers()).isEqualTo(COVERS);
            softly.assertThat(command.mainCourseRecipes()).isEqualTo(MAIN_COURSE_RECIPES);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when values are equal")
    void isEqual(CreateMenu c1, CreateMenu c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when values are equal")
    void sameHashCode(CreateMenu c1, CreateMenu c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var createTodayLunch = new CreateMenu(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE, TODAY_LUNCH_MEAL_TYPE,
                TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES);
        return Stream.of(
                Arguments.of(createTodayLunch, createTodayLunch, true),
                Arguments.of(createTodayLunch, new CreateMenu(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE,
                        TODAY_LUNCH_MEAL_TYPE, TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES), true),
                Arguments.of(createTodayLunch, new CreateMenu(OTHER_OWNER, TODAY_LUNCH_DATE,
                        TODAY_LUNCH_MEAL_TYPE, TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES), false),
                Arguments.of(createTodayLunch, new CreateMenu(TODAY_LUNCH_OWNER, TOMORROW_DINNER_DATE,
                        TODAY_LUNCH_MEAL_TYPE, TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES), false),
                Arguments.of(createTodayLunch, new CreateMenu(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE,
                        TOMORROW_DINNER_MEAL_TYPE, TODAY_LUNCH_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES), false),
                Arguments.of(createTodayLunch, new CreateMenu(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE,
                        TODAY_LUNCH_MEAL_TYPE, TOMORROW_DINNER_COVERS, TODAY_LUNCH_MAIN_COURSE_RECIPES), false),
                Arguments.of(createTodayLunch, new CreateMenu(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE,
                        TODAY_LUNCH_MEAL_TYPE, TODAY_LUNCH_COVERS, TOMORROW_DINNER_MAIN_COURSE_RECIPES), false)
        );
    }

}
