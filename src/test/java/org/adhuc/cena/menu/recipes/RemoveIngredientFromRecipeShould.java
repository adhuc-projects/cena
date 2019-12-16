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

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
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
 * The {@link RemoveIngredientFromRecipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient from recipe removal command should")
class RemoveIngredientFromRecipeShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, RecipeId recipeId) {
        assertThrows(IllegalArgumentException.class, () -> new RemoveIngredientFromRecipe(ingredientId, recipeId));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, ID),
                Arguments.of(IngredientMother.ID, null)
        );
    }

    @Test
    @DisplayName("contain ingredient and recipe ids used during creation")
    void containCreationValues() {
        var command = removeIngredientCommand();
        assertSoftly(softly -> {
            softly.assertThat(command.ingredientId()).isEqualTo(IngredientMother.ID);
            softly.assertThat(command.recipeId()).isEqualTo(ID);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when both ingredient and recipe ids are equal")
    void isEqual(RemoveIngredientFromRecipe c1, RemoveIngredientFromRecipe c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when both ingredient and recipe ids are equal")
    void sameHashCode(RemoveIngredientFromRecipe c1, RemoveIngredientFromRecipe c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var addTomato = removeIngredientCommand(TOMATO_ID, ID);
        return Stream.of(
                Arguments.of(addTomato, addTomato, true),
                Arguments.of(addTomato, removeIngredientCommand(TOMATO_ID, ID), true),
                Arguments.of(addTomato, removeIngredientCommand(CUCUMBER_ID, ID), false),
                Arguments.of(addTomato, removeIngredientCommand(TOMATO_ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID), false)
        );
    }

}
