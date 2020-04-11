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
package org.adhuc.cena.menu.steps.serenity.recipes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import net.serenitybdd.core.Serenity;

/**
 * A recipe storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public final class RecipeStorageDelegate {

    private static final String ASSUMED_RECIPES_SESSION_KEY = "assumedRecipes";
    private static final String RECIPES_SESSION_KEY = "recipes";
    private static final String RECIPE_SESSION_KEY = "recipe";

    /**
     * Stores the recipes.
     *
     * @param recipes the recipes to store.
     * @return the recipes.
     */
    public Collection<RecipeValue> storeRecipes(Collection<RecipeValue> recipes) {
        return storeRecipes(RECIPES_SESSION_KEY, recipes);
    }

    /**
     * Stores the assumed recipes.
     *
     * @param recipes the recipes to store.
     * @return the recipes.
     */
    public Collection<RecipeValue> storeAssumedRecipes(Collection<RecipeValue> recipes) {
        return storeRecipes(ASSUMED_RECIPES_SESSION_KEY, recipes);
    }

    /**
     * Stores the recipes.
     *
     * @param sessionKey the session key.
     * @param recipes    the recipes to store.
     * @return the recipes.
     */
    Collection<RecipeValue> storeRecipes(String sessionKey, Collection<RecipeValue> recipes) {
        Serenity.setSessionVariable(sessionKey).to(recipes);
        return recipes;
    }

    /**
     * Gets the stored recipes, or fails if recipes were not previously stored.
     *
     * @return the stored recipes.
     * @throws AssertionError if recipes were not previously stored.
     */
    public Collection<RecipeValue> storedRecipes() {
        return storedRecipes(RECIPES_SESSION_KEY);
    }

    /**
     * Gets the stored assumed recipes, or fails if recipes were not previously stored.
     *
     * @return the stored assumed recipes.
     * @throws AssertionError if recipes were not previously stored.
     */
    public Collection<RecipeValue> storedAssumedRecipes() {
        return storedRecipes(ASSUMED_RECIPES_SESSION_KEY);
    }

    /**
     * Gets the stored recipes, or fails if recipes were not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored recipes.
     * @throws AssertionError if recipes were not previously stored.
     */
    Collection<RecipeValue> storedRecipes(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Recipes in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
    }

    /**
     * Stores the recipe.
     *
     * @param recipe the recipe to store.
     * @return the recipe.
     */
    public RecipeValue storeRecipe(RecipeValue recipe) {
        Serenity.setSessionVariable(RECIPE_SESSION_KEY).to(recipe);
        return recipe;
    }

    /**
     * Gets the stored recipe, or fails if recipe was not previously stored.
     *
     * @return the stored recipe.
     * @throws AssertionError if recipe was not previously stored.
     */
    public RecipeValue storedRecipe() {
        RecipeValue recipe = Serenity.sessionVariableCalled(RECIPE_SESSION_KEY);
        assertThat(recipe).isNotNull();
        return recipe;
    }

}
