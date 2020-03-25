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
import static org.adhuc.cena.menu.recipes.MeasurementUnit.UNIT;
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
 * The {@link AddIngredientToRecipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient to recipe addition command should")
class AddIngredientToRecipeShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, RecipeId recipeId, boolean isMainIngredient, Quantity quantity) {
        assertThrows(IllegalArgumentException.class, () -> new AddIngredientToRecipe(ingredientId, recipeId, isMainIngredient, quantity));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, ID, true, QUANTITY),
                Arguments.of(IngredientMother.ID, null, true, QUANTITY),
                Arguments.of(IngredientMother.ID, ID, true, null)
        );
    }

    @Test
    @DisplayName("contain ingredient and recipe ids and quantity used during creation")
    void containCreationValues() {
        var command = addIngredientCommand();
        assertSoftly(softly -> {
            softly.assertThat(command.ingredientId()).isEqualTo(IngredientMother.ID);
            softly.assertThat(command.recipeId()).isEqualTo(ID);
            softly.assertThat(command.isMainIngredient()).isEqualTo(true);
            softly.assertThat(command.quantity()).isEqualTo(QUANTITY);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when both ingredient and recipe ids are equal")
    void isEqual(AddIngredientToRecipe c1, AddIngredientToRecipe c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when both ingredient and recipe ids are equal")
    void sameHashCode(AddIngredientToRecipe c1, AddIngredientToRecipe c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var addTomato = addIngredientCommand(TOMATO_ID, ID, MAIN_INGREDIENT, QUANTITY);
        return Stream.of(
                Arguments.of(addTomato, addTomato, true),
                Arguments.of(addTomato, addIngredientCommand(TOMATO_ID, ID, MAIN_INGREDIENT, QUANTITY), true),
                Arguments.of(addTomato, addIngredientCommand(CUCUMBER_ID, ID, MAIN_INGREDIENT, QUANTITY), false),
                Arguments.of(addTomato, addIngredientCommand(TOMATO_ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, MAIN_INGREDIENT, QUANTITY), false),
                Arguments.of(addTomato, addIngredientCommand(TOMATO_ID, ID, !MAIN_INGREDIENT, QUANTITY), false),
                Arguments.of(addTomato, addIngredientCommand(TOMATO_ID, ID, MAIN_INGREDIENT, new Quantity(3, UNIT)), false)
        );
    }

}
