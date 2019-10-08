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

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception occurring while creating an ingredient with already used name.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Value
@EqualsAndHashCode(callSuper = true)
@ResponseStatus(BAD_REQUEST)
public class IngredientNameAlreadyUsedException extends RuntimeException {
}
