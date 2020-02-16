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

/**
 * An ingredient related object service, able to indicates if an ingredient is related to some object and delete relation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public interface IngredientRelatedService {

    /**
     * Indicates whether at least one ingredient is related to some object.
     *
     * @return {@code true} if at least one ingredient is related to some object, {@¢ode false} otherwise.
     */
    boolean areIngredientsRelated();

    /**
     * Indicates whether the ingredient corresponding to the specified identity is related to some object.
     *
     * @param ingredientId the ingredient identity.
     * @return {@code true} if the ingredient is related to some object, {@code false} otherwise.
     */
    boolean isIngredientRelated(IngredientId ingredientId);

    /**
     * Provides the name of the related objects this retriever works on.
     *
     * @return the name of the related objects.
     */
    String relatedObjectName();

}
