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
import static org.adhuc.cena.menu.recipes.RecipeMother.TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

/**
 * The {@link Menu} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menu should")
class MenuShould {

    @ParameterizedTest
    @NullSource
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(CreateMenu command) {
        assertThrows(NullPointerException.class, () -> new Menu(command));
    }

    @Test
    @DisplayName("contain values used during creation")
    void containCreationValues() {
        var menu = new Menu(createCommand());
        assertSoftly(softly -> {
            softly.assertThat(menu.id()).isEqualTo(ID);
            softly.assertThat(menu.owner()).isEqualTo(OWNER);
            softly.assertThat(menu.date()).isEqualTo(DATE);
            softly.assertThat(menu.mealType()).isEqualTo(MEAL_TYPE);
            softly.assertThat(menu.covers()).isEqualTo(COVERS);
            softly.assertThat(menu.mainCourseRecipes()).isEqualTo(MAIN_COURSE_RECIPES);
        });
    }

    @Test
    @DisplayName("return non modifiable main course recipes set")
    void mainCourseRecipesNotModifiable() {
        var menu = menu();
        assertThrows(UnsupportedOperationException.class, () -> menu.mainCourseRecipes().add(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when menu id is equal")
    void isEqual(Menu m1, Menu m2, boolean expected) {
        assertThat(m1.equals(m2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when menu id is equal")
    void sameHashCode(Menu m1, Menu m2, boolean expected) {
        assertThat(m1.hashCode() == m2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var builder = builder();
        var todayLunch = builder.build();
        return Stream.of(
                Arguments.of(todayLunch, todayLunch, true),
                Arguments.of(todayLunch, builder.build(), true),
                Arguments.of(todayLunch, builder().withOwner(OTHER_OWNER).build(), false),
                Arguments.of(todayLunch, builder().withDate(TOMORROW_DINNER_DATE).build(), false),
                Arguments.of(todayLunch, builder().withMealType(TOMORROW_DINNER_MEAL_TYPE).build(), false),
                Arguments.of(todayLunch, builder().withCovers(TOMORROW_DINNER_COVERS).build(), true),
                Arguments.of(todayLunch, builder().withMainCourseRecipes(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build(), true)
        );
    }

}
