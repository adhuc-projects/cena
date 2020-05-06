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
package org.adhuc.cena.menu.menus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.exception.CenaException;
import org.adhuc.cena.menu.common.exception.ExceptionCode;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * An exception occurring while creating a menu with unknown recipes.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@ResponseStatus(BAD_REQUEST)
public class MenuNotCreatableWithUnknownRecipeException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.MENU_NOT_CREATABLE_WITH_UNKNOWN_RECIPE;

    /**
     * Creates a {@code MenuNotCreatableWithUnknownRecipeException} based on the specified menu and recipe identities.
     *
     * @param recipeIds the recipe identities.
     * @param menuId   the menu identity.
     */
    MenuNotCreatableWithUnknownRecipeException(@NonNull List<RecipeId> recipeIds, @NonNull MenuId menuId) {
        super("Menu scheduled at " + menuId.toScheduleString() + " cannot be created with unknown recipes " + recipeIds, EXCEPTION_CODE);
    }

}
