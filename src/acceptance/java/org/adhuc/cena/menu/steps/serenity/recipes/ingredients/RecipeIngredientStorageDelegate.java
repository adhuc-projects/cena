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
package org.adhuc.cena.menu.steps.serenity.recipes.ingredients;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import net.serenitybdd.core.Serenity;

/**
 * A recipe ingredient storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public final class RecipeIngredientStorageDelegate {

    private static final String ASSUMED_RECIPE_INGREDIENTS_SESSION_KEY = "assumedRecipeIngredients";
    private static final String RECIPE_INGREDIENTS_SESSION_KEY = "recipeIngredients";

    /**
     * Stores the recipe ingredients.
     *
     * @param ingredients the recipe ingredients to store.
     * @return the ingredients.
     */
    public Collection<RecipeIngredientValue> storeRecipeIngredients(Collection<RecipeIngredientValue> ingredients) {
        return storeRecipeIngredients(RECIPE_INGREDIENTS_SESSION_KEY, ingredients);
    }

    /**
     * Stores the assumed recipe ingredients.
     *
     * @param ingredients the recipe ingredients to store.
     * @return the ingredients.
     */
    public Collection<RecipeIngredientValue> storeAssumedRecipeIngredients(Collection<RecipeIngredientValue> ingredients) {
        return storeRecipeIngredients(ASSUMED_RECIPE_INGREDIENTS_SESSION_KEY, ingredients);
    }

    /**
     * Stores the recipe ingredients.
     *
     * @param sessionKey  the session key.
     * @param ingredients the recipe ingredients to store.
     * @return the ingredients.
     */
    Collection<RecipeIngredientValue> storeRecipeIngredients(String sessionKey, Collection<RecipeIngredientValue> ingredients) {
        Serenity.setSessionVariable(sessionKey).to(ingredients);
        return ingredients;
    }

    /**
     * Gets the stored recipe ingredients, or fails if recipe ingredients were not previously stored.
     *
     * @return the stored recipe ingredients.
     * @throws AssertionError if recipe ingredients were not previously stored.
     */
    public Collection<RecipeIngredientValue> storedRecipeIngredients() {
        return storedRecipeIngredients(RECIPE_INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the stored assumed recipe ingredients, or fails if recipe ingredients were not previously stored.
     *
     * @return the stored assumed recipe ingredients.
     * @throws AssertionError if recipe ingredients were not previously stored.
     */
    public Collection<RecipeIngredientValue> storedAssumedRecipeIngredients() {
        return storedRecipeIngredients(ASSUMED_RECIPE_INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the stored recipe ingredients, or fails if recipe ingredients were not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored recipe ingredients.
     * @throws AssertionError if recipe ingredients were not previously stored.
     */
    Collection<RecipeIngredientValue> storedRecipeIngredients(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Recipe ingredients in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
    }

}
