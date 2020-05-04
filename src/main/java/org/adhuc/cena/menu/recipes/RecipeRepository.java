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

import java.util.Collection;

import org.adhuc.cena.menu.common.Repository;
import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * A {@link Recipe} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public interface RecipeRepository extends Repository<Recipe, RecipeId> {

    @Override
    default Class<Recipe> entityType() {
        return Recipe.class;
    }

    /**
     * Finds all the recipes stored in the repository.
     *
     * @return all the recipes.
     */
    Collection<Recipe> findAll();

    /**
     * Finds all the recipes stored in the repository composed of the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity to filter on.
     * @return the recipes composed of the ingredient.
     */
    Collection<Recipe> findByIngredient(IngredientId ingredientId);

    /**
     * Deletes all the recipes stored in the repository.
     */
    void deleteAll();

    /**
     * Deletes the specified recipe.
     *
     * @param recipe the recipe to delete.
     */
    void delete(Recipe recipe);

}
