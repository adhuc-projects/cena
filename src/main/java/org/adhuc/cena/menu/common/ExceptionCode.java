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
package org.adhuc.cena.menu.common;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * The exception codes.
 * <p>
 * Exception code ranges are :
 * <ul>
 * <li>[100000-109999] - General errors</li>
 * <li>...</li>
 * <li>[900000-909999] - Business errors</li>
 * </ul>
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Getter
@Accessors(fluent = true)
public enum ExceptionCode {

    // General errors
    INTERNAL_ERROR(100000, "Internal error"),
    INVALID_REQUEST(101000, "Invalid request"),

    ENTITY_NOT_FOUND(900000, "Entity not found"),
    INGREDIENT_NAME_ALREADY_USED(900100, "Ingredient name already used"),
    INGREDIENT_NOT_DELETABLE_RELATED_TO_OBJECT(900101, "Ingredient cannot be deleted as it is related to at least one other object"),
    INGREDIENT_NOT_RELATED_TO_RECIPE(900110, "Ingredient not related to recipe"),
    INVALID_MEASUREMENT_UNIT_FOR_INGREDIENT(900111, "Invalid measurement unit for ingredient");

    private final int code;
    private final String description;

    /**
     * Constructs an exception code based on the code and description.
     *
     * @param code        the code.
     * @param description the description.
     */
    ExceptionCode(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

}
