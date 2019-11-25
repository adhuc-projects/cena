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

import java.util.Collection;
import java.util.Optional;

import lombok.NonNull;

import org.adhuc.cena.menu.common.EntityNotFoundException;

/**
 * A {@link Recipe} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public interface RecipeRepository {

    /**
     * Finds all the recipes stored in the repository.
     *
     * @return all the recipes.
     */
    Collection<Recipe> findAll();

    /**
     * Finds the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe if existing, empty otherwise.
     */
    Optional<Recipe> findById(RecipeId recipeId);

    /**
     * Finds the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe if existing.
     * @throws EntityNotFoundException if no recipe could be found for identity.
     */
    default Recipe findNotNullById(@NonNull RecipeId recipeId) {
        var recipe = findById(recipeId);
        if (recipe.isPresent()) {
            return recipe.get();
        }
        throw new EntityNotFoundException(Recipe.class, recipeId);
    }

    /**
     * Saves the specified recipe.
     *
     * @param recipe the recipe to save.
     * @return the saved recipe.
     */
    <S extends Recipe> S save(S recipe);

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
