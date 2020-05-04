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

import static java.lang.String.format;

import static org.springframework.http.HttpStatus.CONFLICT;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.CenaException;
import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * An exception occurring while deleting one or more ingredients that are related to other objects.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@ResponseStatus(CONFLICT)
class IngredientNotDeletableRelatedToObjectException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INGREDIENT_NOT_DELETABLE_RELATED_TO_OBJECT;

    /**
     * Creates an {@code IngredientNotDeletableRelatedToObjectException} based on the specified related object name.
     *
     * @param relatedObjectName the related object name.
     */
    IngredientNotDeletableRelatedToObjectException(@NonNull String relatedObjectName) {
        super(format("Ingredients cannot be deleted as at least one is related to at least one %s", relatedObjectName), EXCEPTION_CODE);
    }

    /**
     * Creates an {@code IngredientNotDeletableRelatedToObjectException} based on the specified ingredient identity and
     * related object name.
     *
     * @param ingredientId the ingredient identity.
     * @param relatedObjectName the related object name.
     */
    IngredientNotDeletableRelatedToObjectException(@NonNull IngredientId ingredientId, @NonNull String relatedObjectName) {
        super(format("Ingredient '%s' cannot be deleted as it is related to at least one %s", ingredientId, relatedObjectName), EXCEPTION_CODE);
    }

}
