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
final class RecipeStorageDelegate {

    private static final String RECIPES_SESSION_KEY = "recipes";
    private static final String RECIPE_SESSION_KEY = "recipe";

    /**
     * Stores the recipes.
     *
     * @param recipes the recipes to store.
     * @return the recipes.
     */
    public Collection<RecipeValue> storeRecipes(Collection<RecipeValue> recipes) {
        Serenity.setSessionVariable(RECIPES_SESSION_KEY).to(recipes);
        return recipes;
    }

    /**
     * Gets the stored recipes, or fails if recipes were not previously stored.
     *
     * @return the stored recipes.
     * @throws AssertionError if recipes were not previously stored.
     */
    public Collection<RecipeValue> storedRecipes() {
        assertThat(Serenity.hasASessionVariableCalled(RECIPES_SESSION_KEY))
                .as("Recipes in session (%s) must have been set previously", RECIPES_SESSION_KEY).isTrue();
        return Serenity.sessionVariableCalled(RECIPES_SESSION_KEY);
    }

    /**
     * Stores the ingredient.
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
