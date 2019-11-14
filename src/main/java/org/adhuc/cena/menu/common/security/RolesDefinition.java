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
package org.adhuc.cena.menu.common.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The roles definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RolesDefinition {

    public static final String ACTUATOR_ROLE = "ACTUATOR";
    public static final String USER_ROLE = "USER";
    public static final String INGREDIENT_MANAGER_ROLE = "INGREDIENT_MANAGER";
    public static final String SUPER_ADMINISTRATOR_ROLE = "SUPER_ADMIN";

}
