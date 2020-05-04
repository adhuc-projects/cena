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
package org.adhuc.cena.menu.ingredients;

import java.util.Collection;
import java.util.Optional;

import org.adhuc.cena.menu.common.Name;
import org.adhuc.cena.menu.common.Repository;

/**
 * An {@link Ingredient} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public interface IngredientRepository extends Repository<Ingredient, IngredientId> {

    @Override
    default Class<Ingredient> entityType() {
        return Ingredient.class;
    }

    /**
     * Finds all the ingredients stored in the repository.
     *
     * @return all the ingredients.
     */
    Collection<Ingredient> findAll();

    /**
     * Finds the ingredient corresponding to the specified name.
     *
     * @param ingredientName the ingredient name.
     * @return the ingredient if existing, empty otherwise.
     */
    Optional<Ingredient> findByName(Name ingredientName);

    /**
     * Finds the ingredient corresponding to the specified name ignoring case.
     *
     * @param ingredientName the ingredient name.
     * @return the ingredient if existing, empty otherwise.
     */
    Optional<Ingredient> findByNameIgnoreCase(Name ingredientName);

    /**
     * Deletes all the ingredients stored in the repository.
     */
    void deleteAll();

    /**
     * Deletes the specified ingredient.
     *
     * @param ingredient the ingredient to delete.
     */
    void delete(Ingredient ingredient);

}
