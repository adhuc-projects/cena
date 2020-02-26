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

import java.util.List;

/**
 * An application service for recipes.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public interface RecipeAppService {

    /**
     * Gets the recipes.
     *
     * @param query the query on recipes list, containing filters.
     * @return the recipes (not modifiable).
     */
    List<Recipe> getRecipes(QueryRecipes query);

    /**
     * Gets the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe.
     * @throws org.adhuc.cena.menu.common.EntityNotFoundException if no recipe corresponds to identity.
     */
    Recipe getRecipe(RecipeId recipeId);

    /**
     * Creates a recipe.
     *
     * @param command the recipe creation command.
     */
    Recipe createRecipe(CreateRecipe command);

    /**
     * Deletes the recipes.
     */
    void deleteRecipes();

    /**
     * Deletes a recipe.
     *
     * @param command the recipe deletion command.
     */
    void deleteRecipe(DeleteRecipe command);

}
