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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.MeasurementUnit.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.common.aggregate.Name;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * The {@link Recipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe should")
class RecipeShould {

    @Test
    @DisplayName("not be creatable with null creation command")
    void notBeCreatableWithNullCommand() {
        assertThrows(NullPointerException.class, () -> new Recipe(null));
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(RecipeId recipeId, Name name, String content, RecipeAuthor author,
                                             Servings servings, Set<CourseType> courseTypes) {
        assertThrows(IllegalArgumentException.class, () -> new Recipe(recipeId, name, content, author, servings, courseTypes));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, CONTENT, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, null, CONTENT, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, null, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, "", AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, null, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, null, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, SERVINGS, null)
        );
    }

    @Test
    @DisplayName("contain values from command used during creation")
    void containCreationValuesFromCommand() {
        var recipe = new Recipe(createCommand());
        assertSoftly(softly -> {
            softly.assertThat(recipe.id()).isEqualTo(ID);
            softly.assertThat(recipe.name()).isEqualTo(NAME);
            softly.assertThat(recipe.content()).isEqualTo(CONTENT);
            softly.assertThat(recipe.ingredients()).isEmpty();
            softly.assertThat(recipe.author()).isEqualTo(AUTHOR);
            softly.assertThat(recipe.servings()).isEqualTo(SERVINGS);
            softly.assertThat(recipe.courseTypes()).isEqualTo(COURSE_TYPES);
        });
    }

    @Test
    @DisplayName("return non modifiable course types set")
    void courseTypesNotModifiable() {
        var recipe = recipe();
        assertThrows(UnsupportedOperationException.class, () -> recipe.courseTypes().add(CourseType.DESSERT));
    }

    @Test
    @DisplayName("contain values used during creation")
    void containCreationValues() {
        var recipe = builder().build();
        assertSoftly(softly -> {
            softly.assertThat(recipe.id()).isEqualTo(ID);
            softly.assertThat(recipe.name()).isEqualTo(NAME);
            softly.assertThat(recipe.content()).isEqualTo(CONTENT);
            softly.assertThat(recipe.ingredients()).isEmpty();
            softly.assertThat(recipe.author()).isEqualTo(AUTHOR);
            softly.assertThat(recipe.servings()).isEqualTo(SERVINGS);
            softly.assertThat(recipe.courseTypes()).isEqualTo(COURSE_TYPES);
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
                addIngredientCommand(IngredientMother.ID, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID, MAIN_INGREDIENT, QUANTITY)));
        assertThat(exception.getMessage()).isEqualTo("Wrong command recipe identity " +
                TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID + " to add ingredient to recipe with identity " + ID);
    }

    @Test
    @DisplayName("return non modifiable ingredients set")
    void ingredientsNotModifiable() {
        var recipe = builder().withIngredients(CUCUMBER_ID).build();
        assertThrows(UnsupportedOperationException.class, () -> recipe.ingredients().add(recipeIngredient()));
    }

    @ParameterizedTest
    @MethodSource("addIngredientToRecipeParameters")
    @DisplayName("be related to ingredient after adding ingredient to recipe")
    void addIngredientToRecipe(IngredientId ingredientId, boolean isMainIngredient, Quantity quantity) {
        var recipeIngredient = recipeIngredient(ingredientId, isMainIngredient, quantity);
        var recipe = builder().build();
        assumeThat(recipe.ingredients()).isEmpty();

        recipe.addIngredient(addIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).isNotEmpty().containsExactly(recipeIngredient);
    }

    private static Stream<Arguments> addIngredientToRecipeParameters() {
        return Stream.of(
                Arguments.of(TOMATO_ID, MAIN_INGREDIENT, QUANTITY),
                Arguments.of(TOMATO_ID, false, QUANTITY),
                Arguments.of(TOMATO_ID, MAIN_INGREDIENT, new Quantity(1, KILOGRAM)),
                Arguments.of(CUCUMBER_ID, MAIN_INGREDIENT, new Quantity(3, UNIT))
        );
    }

    @Test
    @DisplayName("be related to ingredient after adding ingredient to recipe twice")
    void addIngredientToRecipeTwice() {
        var recipeIngredient = recipeIngredient(TOMATO_ID, MAIN_INGREDIENT, QUANTITY);
        var recipe = builder().withIngredients(TOMATO_ID).build();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.addIngredient(addIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).isNotEmpty().containsExactly(recipeIngredient);
    }

    @Test
    @DisplayName("be related to multiple ingredients after adding ingredient to recipe already related to ingredients")
    void addIngredientToRecipeMultiple() {
        var recipeIngredient = recipeIngredient(IngredientId.generate(), true, new Quantity(5, CENTILITER));
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
        var recipeIngredient = recipeIngredient(TOMATO_ID, MAIN_INGREDIENT, QUANTITY);
        var recipe = recipe();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.removeIngredient(removeIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).doesNotContain(recipeIngredient);
    }

    @Test
    @DisplayName("be related to non removed ingredients after removing ingredient from recipe")
    void removeIngredientFromRecipeOthersStillRelated() {
        var recipeIngredient = recipeIngredient(TOMATO_ID, MAIN_INGREDIENT, QUANTITY);
        var recipe = builder().withIngredients(TOMATO_ID, CUCUMBER_ID).build();
        assumeThat(recipe.ingredients()).contains(recipeIngredient);

        recipe.removeIngredient(removeIngredientCommand(recipeIngredient));
        assertThat(recipe.ingredients()).doesNotContain(recipeIngredient).contains(recipeIngredient(CUCUMBER_ID, MAIN_INGREDIENT, QUANTITY));
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
    @DisplayName("return false when checking if recipe is composed of unknown ingredient")
    void isNotComposedOfUnknownIngredient() {
        var recipe = recipe();
        var unknownIngredientId = IngredientId.generate();
        assertThat(recipe.isComposedOf(unknownIngredientId)).isFalse();
    }

    @Test
    @DisplayName("throw EntityNotFoundException when retrieving unknown ingredient")
    void unknownIngredient() {
        var recipe = recipe();
        var unknownIngredientId = IngredientId.generate();
        assertThrows(EntityNotFoundException.class, () -> recipe.ingredient(unknownIngredientId));
    }

    @Test
    @DisplayName("return true when checking if recipe is composed of known ingredient")
    void isComposedOfKnownIngredient() {
        var recipe = recipe();
        recipe.ingredient(TOMATO_ID);
        assertThat(recipe.isComposedOf(TOMATO_ID)).isTrue();
    }

    @Test
    @DisplayName("return the ingredient when retrieving related ingredient")
    void knownIngredient() {
        var recipe = recipe();
        var ingredient = recipe.ingredient(TOMATO_ID);
        assertThat(ingredient).isNotNull().isEqualTo(recipeIngredient(TOMATO_ID, MAIN_INGREDIENT, QUANTITY));
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
        var builder = builder();
        var tomatoCucumberMozzaSalad = builder.build();
        return Stream.of(
                Arguments.of(tomatoCucumberMozzaSalad, tomatoCucumberMozzaSalad, true),
                Arguments.of(tomatoCucumberMozzaSalad, builder.build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withServings(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_SERVINGS).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withCourseTypes(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_COURSE_TYPES).build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withIngredients().build(), true),
                Arguments.of(tomatoCucumberMozzaSalad, builder().withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID).build(), false)
        );
    }

}
