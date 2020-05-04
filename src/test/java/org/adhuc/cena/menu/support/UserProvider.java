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
package org.adhuc.cena.menu.support;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

/**
 * A user provider, for testing purposes.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class UserProvider {

    public static final String AUTHENTICATED_USER = "user";
    public static final String INGREDIENT_MANAGER = "ingredient-manager";
    public static final String SUPER_ADMINISTRATOR = "super-admin";

}
