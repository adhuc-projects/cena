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

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * The {@link RecipeIngredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe ingredient should")
class RecipeIngredientShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(RecipeId recipeId, IngredientId ingredientId) {
        assertThrows(IllegalArgumentException.class, () -> new RecipeIngredient(recipeId, ingredientId));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, IngredientMother.ID),
                Arguments.of(ID, null)
        );
    }

    @Test
    @DisplayName("contain recipe and ingredient identities used during construction")
    void containCreationValue() {
        var recipeIngredient = new RecipeIngredient(ID, IngredientMother.ID);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(recipeIngredient.recipeId()).isEqualTo(ID);
            softAssertions.assertThat(recipeIngredient.ingredientId()).isEqualTo(IngredientMother.ID);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when recipe and ingredient identities are equal")
    void isEqual(RecipeIngredient r1, RecipeIngredient r2, boolean expected) {
        assertThat(r1.equals(r2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when recipe and ingredient identities are equal")
    void sameHashCode(RecipeIngredient r1, RecipeIngredient r2, boolean expected) {
        assertThat(r1.hashCode() == r2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var recipeIngredient = new RecipeIngredient(TOMATO_CUCUMBER_MOZZA_SALAD_ID, IngredientMother.TOMATO_ID);
        return Stream.of(
                Arguments.of(recipeIngredient, recipeIngredient, true),
                Arguments.of(recipeIngredient, new RecipeIngredient(TOMATO_CUCUMBER_MOZZA_SALAD_ID, IngredientMother.TOMATO_ID), true),
                Arguments.of(recipeIngredient, new RecipeIngredient(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, IngredientMother.TOMATO_ID), false),
                Arguments.of(recipeIngredient, new RecipeIngredient(TOMATO_CUCUMBER_MOZZA_SALAD_ID, IngredientMother.CUCUMBER_ID), false)
        );
    }

}
