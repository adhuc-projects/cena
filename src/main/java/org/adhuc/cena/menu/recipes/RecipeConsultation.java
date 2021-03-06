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

import java.util.List;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;

/**
 * An application service for recipes consultation. Recipe consultation provides query methods available for every user.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public interface RecipeConsultation {

    /**
     * Gets the recipes.
     *
     * @param query the query on recipes list, containing filters.
     * @return the recipes (not modifiable).
     */
    List<Recipe> getRecipes(QueryRecipes query);

    /**
     * Indicates whether a recipe with the specified identity exists.
     *
     * @param recipeId the recipe identity.
     * @return {@code true} if recipe exists, {@code false} otherwise.
     */
    boolean exists(RecipeId recipeId);

    /**
     * Gets the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe.
     * @throws EntityNotFoundException if no recipe corresponds to identity.
     */
    Recipe getRecipe(RecipeId recipeId);

}
