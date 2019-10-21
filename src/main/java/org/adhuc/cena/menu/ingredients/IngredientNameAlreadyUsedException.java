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

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.CenaException;
import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * An exception occurring while creating an ingredient with already used name.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@ResponseStatus(BAD_REQUEST)
public class IngredientNameAlreadyUsedException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INGREDIENT_NAME_ALREADY_USED;

    /**
     * Creates an {@code IngredientNameAlreadyUsedException} based on the specified ingredient name.
     *
     * @param ingredientName the already used ingredient name.
     */
    public IngredientNameAlreadyUsedException(@NonNull String ingredientName) {
        super(String.format("Ingredient name '%s' already used by an existing ingredient", ingredientName), EXCEPTION_CODE);
    }

}
