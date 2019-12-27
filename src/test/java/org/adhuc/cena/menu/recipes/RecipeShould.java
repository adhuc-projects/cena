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
import static org.assertj.core.api.Assumptions.assumeThat;
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

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

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
    void notBeCreatableWithInvalidParameters(RecipeId recipeId, String name, String content, RecipeAuthor author) {
        assertThrows(IllegalArgumentException.class, () -> new Recipe(recipeId, name, content, author));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, CONTENT, AUTHOR),
                Arguments.of(ID, null, CONTENT, AUTHOR),
                Arguments.of(ID, "", CONTENT, AUTHOR),
                Arguments.of(ID, NAME, null, AUTHOR),
                Arguments.of(ID, NAME, "", AUTHOR),
                Arguments.of(ID, NAME, CONTENT, null)
        );
    }

    @Test
    @DisplayName("contain id, name, content and author used during creation")
    void containCreationValues() {
        var recipe = builder().build();
        assertSoftly(softly -> {
            softly.assertThat(recipe.id()).isEqualTo(ID);
            softly.assertThat(recipe.name()).isEqualTo(NAME);
            softly.assertThat(recipe.content()).isEqualTo(CONTENT);
            softly.assertThat(recipe.ingredients()).isEmpty();
            softly.assertThat(recipe.author()).isEqualTo(AUTHOR);
        });
    }

    @Test
    @DisplayName("throw IllegalArgumentException when adding ingredient from null command")
    void addIngredientNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> recipe().addIngredient(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when adding ingredient to recipe with wrong id")
    void addIngredientWrongRecipeId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> recipe().addIngredient(
                addIngredientCommand(IngredientMother.ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
        assertThat(exception.getMessage()).isEqualTo("Wrong command recipe identity " +
                TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID + " to add ingredient to recipe with identity " + ID);
    }

    @Test
    @DisplayName("return non modifiable ingredients set")
    void ingredientsNotModifiable() {
        var recipe = builder().withIngredients(CUCUMBER_ID).build();
        assertThrows(UnsupportedOperationException.class, () -> recipe.ingredients().add(recipeIngredient()));
    }

    @Test
    @DisplayName("be related to ingredient after adding ingredient to recipe")
    void addIngredientToRecipe() {
        var recipeIngredient = recipeIngredient(TOMATO_ID);
        var recipe = builder().build();
        assumeThat(recipe.ingredients()).isEmpty();

        recipe.addIngredient(addIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).isNotEmpty().containsExactly(recipeIngredient);
    }

    @Test
    @DisplayName("be related to ingredient after adding ingredient to recipe twice")
    void addIngredientToRecipeTwice() {
        var recipeIngredient = recipeIngredient(TOMATO_ID);
        var recipe = builder().withIngredients(TOMATO_ID).build();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.addIngredient(addIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).isNotEmpty().containsExactly(recipeIngredient);
    }

    @Test
    @DisplayName("be related to multiple ingredients after adding ingredient to recipe already related to ingredients")
    void addIngredientToRecipeMultiple() {
        var recipeIngredient = recipeIngredient(IngredientId.generate());
        var recipe = builder().withIngredients(TOMATO_ID, CUCUMBER_ID).build();
        assumeThat(recipe.ingredients()).isNotEmpty().doesNotContain(recipeIngredient);
        var existingIngredients = recipe.ingredients();

        recipe.addIngredient(addIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).isNotEmpty().containsAll(existingIngredients).contains(recipeIngredient);
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredient from null command")
    void removeIngredientNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> recipe().removeIngredient(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredient from recipe with wrong id")
    void removeIngredientWrongRecipeId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> recipe().removeIngredient(
                removeIngredientCommand(IngredientMother.ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
        assertThat(exception.getMessage()).isEqualTo("Wrong command recipe identity " +
                TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID + " to remove ingredient from recipe with identity " + ID);
    }

    @Test
    @DisplayName("throw IngredientNotRelatedToRecipeException when removing non related ingredient from recipe")
    void removeUnknownIngredient() {
        var recipe = builder().withIngredients(CUCUMBER_ID).build();
        var exception = assertThrows(IngredientNotRelatedToRecipeException.class, () ->
                recipe.removeIngredient(removeIngredientCommand(TOMATO_ID)));
        assertThat(exception.getMessage()).isEqualTo("Ingredient '" + TOMATO_ID + "' is not related to recipe '" + ID + "'");
    }

    @Test
    @DisplayName("not be related to ingredient after removing ingredient from recipe")
    void removeIngredientFromRecipe() {
        var recipeIngredient = recipeIngredient(TOMATO_ID);
        var recipe = recipe();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.removeIngredient(removeIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).doesNotContain(recipeIngredient);
    }

    @Test
    @DisplayName("be related to non removed ingredients after removing ingredient from recipe")
    void removeIngredientFromRecipeOthersStillRelated() {
        var recipeIngredient = recipeIngredient(TOMATO_ID);
        var recipe = builder().withIngredients(TOMATO_ID, CUCUMBER_ID).build();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.removeIngredient(removeIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).doesNotContain(recipeIngredient).contains(recipeIngredient(CUCUMBER_ID));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredients from null command")
    void removeIngredientsNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> recipe().removeIngredients(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredients from recipe with wrong id")
    void removeIngredientsWrongRecipeId() {
        var exception = assertThrows(IllegalArgumentException.class, () -> recipe().removeIngredients(
                new RemoveIngredientsFromRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
        assertThat(exception.getMessage()).isEqualTo("Wrong command recipe identity " +
                TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID + " to remove ingredients from recipe with identity " + ID);
    }

    @Test
    @DisplayName("have no more related ingredients after removing all related ingredients")
    void removeIngredientsFromRecipe() {
        var recipe = recipe();
        assumeThat(recipe.ingredients()).isNotEmpty();

        recipe.removeIngredients(new RemoveIngredientsFromRecipe(ID));
        assertThat(recipe.ingredients()).isEmpty();
    }

    @Test
    @DisplayName("throw EntityNotFoundException when retrieving unknown ingredient")
    void unknownIngredient() {
        var recipe = recipe();
        var unknownIngredientId = IngredientId.generate();
        assertThrows(EntityNotFoundException.class, () -> recipe.ingredient(unknownIngredientId));
    }

    @Test
    @DisplayName("return the ingredient when retrieving related ingredient")
    void knownIngredient() {
        var recipe = recipe();
        var ingredient = recipe.ingredient(TOMATO_ID);
        assertThat(ingredient).isNotNull().isEqualTo(recipeIngredient(TOMATO_ID));
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
        var builder = builder()
                .withId(TOMATO_CUCUMBER_MOZZA_SALAD_ID)
                .withName(TOMATO_CUCUMBER_MOZZA_SALAD_NAME)
                .withContent(TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT)
                .withAuthor(TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR)
                .withIngredients(TOMATO_ID, CUCUMBER_ID);
        var tomatoCucumberMozzaSalad = builder.build();
        return Stream.of(
                Arguments.of(tomatoCucumberMozzaSalad, tomatoCucumberMozzaSalad, true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.withIngredients().build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build(), false)
        );
    }

}
