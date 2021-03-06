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

/**
 * An application service for ingredients consultation. This service provides query methods available for every user.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
public interface IngredientConsultation {

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
     * @throws org.adhuc.cena.menu.common.aggregate.EntityNotFoundException if no ingredient corresponds to identity.
     */
    Ingredient getIngredient(IngredientId ingredientId);

}
