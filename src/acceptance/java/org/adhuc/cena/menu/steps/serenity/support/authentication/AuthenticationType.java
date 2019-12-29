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
package org.adhuc.cena.menu.steps.serenity.support.authentication;

import static lombok.AccessLevel.PRIVATE;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The authentication types.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor(access = PRIVATE)
public enum AuthenticationType {

    COMMUNITY_USER(new AnonymousAuthentication()),
    ACTUATOR_MANAGER(new BasicAuthentication("actuator", "actuator")),
    AUTHENTICATED_USER(new BasicAuthentication("user", "password")),
    INGREDIENT_MANAGER(new BasicAuthentication("ingredient-manager", "ingredient-manager")),
    SUPER_ADMINISTRATOR(new BasicAuthentication("super-admin", "super-admin"));

    private Authentication authentication;

    public Supplier<Authentication> getAuthenticationSupplier() {
        return () -> authentication;
    }

    @Override
    public String toString() {
        return authentication.toString();
    }
}
