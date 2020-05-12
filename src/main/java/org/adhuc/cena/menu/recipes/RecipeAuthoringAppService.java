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

import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;

/**
 * An application service for recipe authoring. Recipe authoring provides command methods for recipe manipulation by its
 * author and is available only for authenticated users that are recipe authors, or super administrator.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public interface RecipeAuthoringAppService {

    /**
     * Creates a recipe.
     *
     * @param command the recipe creation command.
     * @throws AlreadyExistingEntityException if a recipe already exists with the identity specified in creation command.
     */
    void createRecipe(CreateRecipe command);

    /**
     * Deletes a recipe.
     *
     * @param command the recipe deletion command.
     */
    void deleteRecipe(DeleteRecipe command);

    /**
     * Adds an ingredient to a recipe.
     *
     * @param command the ingredient to recipe addition command.
     */
    void addIngredientToRecipe(AddIngredientToRecipe command);

    /**
     * Removes an ingredient from a recipe.
     *
     * @param command the ingredient from recipe removal command.
     */
    void removeIngredientFromRecipe(RemoveIngredientFromRecipe command);

    /**
     * Removes all ingredients from a recipe.
     *
     * @param command the ingredients recipe removal command.
     */
    void removeIngredientsFromRecipe(RemoveIngredientsFromRecipe command);

}
