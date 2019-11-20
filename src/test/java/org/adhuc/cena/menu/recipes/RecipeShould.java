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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link Recipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe should")
class RecipeShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(RecipeId recipeId, String name, String content) {
        assertThrows(IllegalArgumentException.class, () -> new Recipe(recipeId, name, content));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, CONTENT),
                Arguments.of(ID, null, CONTENT),
                Arguments.of(ID, "", CONTENT),
                Arguments.of(ID, NAME, null),
                Arguments.of(ID, NAME, "")
        );
    }

    @Test
    @DisplayName("contain id, name and content used during creation")
    void containCreationValues() {
        var recipe = recipe();
        assertSoftly(softly -> {
            softly.assertThat(recipe.id()).isEqualTo(ID);
            softly.assertThat(recipe.name()).isEqualTo(NAME);
            softly.assertThat(recipe.content()).isEqualTo(CONTENT);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when recipe id is equal")
    void isEqual(Recipe r1, Recipe r2, boolean expected) {
        assertThat(r1.equals(r2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when recipe id is equal")
    void sameHashCode(Recipe r1, Recipe r2, boolean expected) {
        assertThat(r1.hashCode() == r2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var tomatoCucumberMozzaSalad = recipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID, TOMATO_CUCUMBER_MOZZA_SALAD_NAME,
                TOMATO_CUCUMER_MOZZA_SALAD_CONTENT);
        return Stream.of(
                Arguments.of(tomatoCucumberMozzaSalad, tomatoCucumberMozzaSalad, true),
                Arguments.of(tomatoCucumberMozzaSalad, recipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMER_MOZZA_SALAD_CONTENT), true),
                Arguments.of(tomatoCucumberMozzaSalad, recipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT), true),
                Arguments.of(tomatoCucumberMozzaSalad, recipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMER_MOZZA_SALAD_CONTENT), false)
        );
    }

}
