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

import static java.lang.String.format;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.Collection;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.exception.CenaException;
import org.adhuc.cena.menu.common.exception.ExceptionCode;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.MeasurementType;

/**
 * An exception occurring while adding an ingredient to a recipe with a {@link Quantity quantity}'s
 * {@link MeasurementUnit measurement unit} that does not correspond to one of the
 * {@link MeasurementType measurement types} of the ingredient.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@ResponseStatus(BAD_REQUEST)
class InvalidMeasurementUnitForIngredientException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_MEASUREMENT_UNIT_FOR_INGREDIENT;

    /**
     * Creates an {@code InvalidMeasurementUnitForIngredientException} based on the specified ingredient and recipe
     * identities and measurement unit and types.
     *
     * @param ingredientId     the ingredient identity.
     * @param recipeId         the recipe identity.
     * @param measurementUnit  the measurement unit.
     * @param measurementTypes the ingredient's measurement types.
     */
    InvalidMeasurementUnitForIngredientException(@NonNull IngredientId ingredientId,
                                                 @NonNull RecipeId recipeId,
                                                 @NonNull MeasurementUnit measurementUnit,
                                                 @NonNull Collection<MeasurementType> measurementTypes) {
        super(format("Unable to add ingredient '%s' to recipe '%s': measurement unit %s does not correspond to ingredient's measurement types %s",
                ingredientId, recipeId, measurementUnit, measurementTypes), EXCEPTION_CODE);
    }

}
