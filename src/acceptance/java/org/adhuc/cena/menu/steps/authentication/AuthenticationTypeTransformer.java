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
package org.adhuc.cena.menu.steps.authentication;

import java.util.Map;

import cucumber.api.Transformer;

import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * A {@link Transformer} implementation for {@link AuthenticationType}s.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class AuthenticationTypeTransformer extends Transformer<AuthenticationType> {

    private static Map<String, AuthenticationType> transformations = Map.of(
            "user", AuthenticationType.AUTHENTICATED_USER,
            "ingredient manager", AuthenticationType.INGREDIENT_MANAGER,
            "actuator manager", AuthenticationType.ACTUATOR_MANAGER,
            "super administrator", AuthenticationType.SUPER_ADMINISTRATOR
    );

    @Override
    public AuthenticationType transform(String value) {
        if (transformations.containsKey(value)) {
            return transformations.get(value);
        }
        throw new IllegalArgumentException("Cannot transform " + value + " into a valid authentication type");
    }

}
