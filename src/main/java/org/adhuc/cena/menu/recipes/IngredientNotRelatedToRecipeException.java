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

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.CenaException;
import org.adhuc.cena.menu.common.ExceptionCode;
import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * An exception occurring while accessing an ingredient that is not related to a recipe.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@ResponseStatus(NOT_FOUND)
class IngredientNotRelatedToRecipeException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INGREDIENT_NOT_RELATED_TO_RECIPE;

    /**
     * Creates an {@code IngredientNotRelatedToRecipeException} based on the specified ingredient and recipe identities.
     *
     * @param ingredientId the ingredient identity.
     * @param recipeId     the recipe identity.
     */
    IngredientNotRelatedToRecipeException(@NonNull IngredientId ingredientId, @NonNull RecipeId recipeId) {
        super(String.format("Ingredient '%s' is not related to recipe '%s'", ingredientId, recipeId), EXCEPTION_CODE);
    }

}
