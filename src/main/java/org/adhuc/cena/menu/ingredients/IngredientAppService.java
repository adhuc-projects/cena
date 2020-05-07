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

import java.util.List;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;

/**
 * An application service for ingredients.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
public interface IngredientAppService {

    /**
     * Gets the ingredients.
     *
     * @return the ingredients (not modifiable).
     */
    List<Ingredient> getIngredients();

    /**
     * Gets the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient.
     * @throws EntityNotFoundException if no ingredient corresponds to identity.
     */
    Ingredient getIngredient(IngredientId ingredientId);

    /**
     * Creates an ingredient.
     *
     * @param command the ingredient creation command.
     * @throws IngredientNameAlreadyUsedException if the ingredient name specified in creation command is already used
     *                                            by another ingredient.
     */
    Ingredient createIngredient(CreateIngredient command);

    /**
     * Deletes the ingredients.
     *
     * @throws IngredientNotDeletableRelatedToObjectException if at least one ingredient is related to another object.
     */
    void deleteIngredients();

    /**
     * Deletes an ingredient.
     *
     * @param command the ingredient deletion command.
     * @throws IngredientNotDeletableRelatedToObjectException if the ingredient is related to another object.
     */
    void deleteIngredient(DeleteIngredient command);
}
