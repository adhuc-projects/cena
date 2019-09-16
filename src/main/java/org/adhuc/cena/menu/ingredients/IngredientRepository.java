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
package org.adhuc.cena.menu.ingredients;

import java.util.Collection;
import java.util.Optional;

import lombok.NonNull;

import org.adhuc.cena.menu.common.EntityNotFoundException;

/**
 * An {@link Ingredient} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public interface IngredientRepository {

    /**
     * Finds all the ingredients stored in the repository.
     *
     * @return all the ingredients.
     */
    Collection<Ingredient> findAll();

    /**
     * Finds the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient if existing, empty otherwise.
     */
    Optional<Ingredient> findById(IngredientId ingredientId);

    /**
     * Finds the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient if existing.
     * @throws EntityNotFoundException if no ingredient could be found for identity.
     */
    default Ingredient findNotNullById(@NonNull IngredientId ingredientId) {
        var ingredient = findById(ingredientId);
        if (ingredient.isPresent()) {
            return ingredient.get();
        }
        throw new EntityNotFoundException(Ingredient.class, ingredientId);
    }

    /**
     * Saves the specified ingredient.
     *
     * @param ingredient the ingredient to save.
     * @return the saved ingredient.
     */
    <S extends Ingredient> S save(S ingredient);

    /**
     * Deletes all the ingredients stored in the repository.
     */
    void deleteAll();
}
