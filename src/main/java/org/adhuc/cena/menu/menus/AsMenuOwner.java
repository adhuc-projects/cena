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

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Indicates that a service class or method is accessible only to menu's owner.
 * <p>
 * Using this annotation requires the query parameter used to call the service to be named {@code ownedBy} or be
 * annotated with {@link org.springframework.security.core.parameters.P @P("ownedBy")} and to be an implementation of
 * {@link OwnedBy}.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @see org.springframework.security.core.parameters.P
 * @since 0.3.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize("isMenuOwner(#ownedBy)")
@interface AsMenuOwner {

}
