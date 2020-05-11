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

import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;
import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;

/**
 * An application service for ingredients management. Ingredient management provides command methods for ingredient
 * manipulation and is available only for ingredient manager or super administrator.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public interface IngredientManagementAppService {

    /**
     * Creates an ingredient.
     *
     * @param command the ingredient creation command.
     * @throws AlreadyExistingEntityException     if an ingredient already exists with the identity specified in creation command.
     * @throws IngredientNameAlreadyUsedException if the ingredient name specified in creation command is already used
     *                                            by another ingredient.
     */
    void createIngredient(CreateIngredient command);

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
     * @throws EntityNotFoundException                        if no ingredient corresponds to identity.
     * @throws IngredientNotDeletableRelatedToObjectException if the ingredient is related to another object.
     */
    void deleteIngredient(DeleteIngredient command);

}
