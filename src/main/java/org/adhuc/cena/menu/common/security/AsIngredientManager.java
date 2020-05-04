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
package org.adhuc.cena.menu.common.security;

import static org.adhuc.cena.menu.common.security.RolesDefinition.INGREDIENT_MANAGER_ROLE;
import static org.adhuc.cena.menu.common.security.RolesDefinition.SUPER_ADMINISTRATOR_ROLE;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Indicates that a service class or method is accessible only to ingredient managers.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("hasRole('" + INGREDIENT_MANAGER_ROLE + "') || hasRole('" + SUPER_ADMINISTRATOR_ROLE + "')")
public @interface AsIngredientManager {
}
