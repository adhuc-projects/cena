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

import static lombok.AccessLevel.PRIVATE;

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * An object mother to create testing domain elements related to {@link Recipe}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.2.0
 */
public class RecipeMother {

    public static final RecipeId TOMATO_CUCUMBER_MOZZA_SALAD_ID = new RecipeId("d71e2fc7-09e3-4241-97a5-dc3383d35e98");
    public static final String TOMATO_CUCUMBER_MOZZA_SALAD_NAME = "Tomato, cucumber and mozzarella salad";
    public static final String TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT = "Cut everything into dices, mix it, dress it";

    public static final RecipeId TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID = new RecipeId("6ef45220-0e64-4303-9f71-ced6cefa6834");
    public static final String TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME = "Tomato, cucumber, olive and feta salad";
    public static final String TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT = "Stone olives, cut everything into dices, mix it, dress it";

    public static final RecipeId ID = TOMATO_CUCUMBER_MOZZA_SALAD_ID;
    public static final String NAME = TOMATO_CUCUMBER_MOZZA_SALAD_NAME;
    public static final String CONTENT = TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT;

    public static CreateRecipe createCommand() {
        return createCommand(recipe());
    }

    public static CreateRecipe createCommand(@NonNull Recipe recipe) {
        return new CreateRecipe(recipe.id(), recipe.name(), recipe.content());
    }

    public static DeleteRecipe deleteCommand() {
        return deleteCommand(TOMATO_CUCUMBER_MOZZA_SALAD_ID);
    }

    public static DeleteRecipe deleteCommand(@NonNull RecipeId recipeId) {
        return new DeleteRecipe(recipeId);
    }

    public static AddIngredientToRecipe addIngredientCommand() {
        return addIngredientCommand(recipeIngredient());
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull RecipeIngredient recipeIngredient) {
        return addIngredientCommand(recipeIngredient.ingredientId(), recipeIngredient.recipeId());
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull IngredientId ingredientId) {
        return addIngredientCommand(ingredientId, ID);
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull IngredientId ingredientId, @NonNull RecipeId recipeId) {
        return new AddIngredientToRecipe(ingredientId, recipeId);
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand() {
        return removeIngredientCommand(recipeIngredient());
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull RecipeIngredient recipeIngredient) {
        return removeIngredientCommand(recipeIngredient.ingredientId(), recipeIngredient.recipeId());
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull IngredientId ingredientId) {
        return removeIngredientCommand(ingredientId, ID);
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull IngredientId ingredientId, @NonNull RecipeId recipeId) {
        return new RemoveIngredientFromRecipe(ingredientId, recipeId);
    }

    public static RemoveIngredientsFromRecipe removeIngredientsCommand() {
        return new RemoveIngredientsFromRecipe(ID);
    }

    public static Recipe recipe() {
        return builder().withIngredients(TOMATO_ID, CUCUMBER_ID).build();
    }

    public static Collection<Recipe> recipes() {
        var tomatoCucumberMozzaSalad = recipe();
        var tomatoCucumberOliveAndFetaSalad = builder()
                .withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)
                .withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME)
                .withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT)
                .withIngredients(TOMATO_ID)
                .build();
        return List.of(tomatoCucumberMozzaSalad, tomatoCucumberOliveAndFetaSalad);
    }

    public static RecipeIngredient recipeIngredient() {
        return recipeIngredient(IngredientMother.ID);
    }

    public static RecipeIngredient recipeIngredient(@NonNull IngredientId ingredientId) {
        return recipeIngredient(ID, ingredientId);
    }

    public static RecipeIngredient recipeIngredient(@NonNull RecipeId recipeId, @NonNull IngredientId ingredientId) {
        return new RecipeIngredient(recipeId, ingredientId);
    }

    public static Builder builder() {
        return new Builder();
    }

    @With
    @NoArgsConstructor(access = PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    public static class Builder {
        private RecipeId id = ID;
        private String name = NAME;
        private String content = CONTENT;
        private Set<IngredientId> ingredients = Set.of();

        public Builder withIngredients(@NonNull IngredientId... ids) {
            return new Builder(id, name, content, Set.of(ids));
        }

        public Recipe build() {
            var recipe = new Recipe(id, name, content);
            ingredients.forEach(id -> recipe.addIngredient(addIngredientCommand(id, recipe.id())));
            return recipe;
        }
    }

}
