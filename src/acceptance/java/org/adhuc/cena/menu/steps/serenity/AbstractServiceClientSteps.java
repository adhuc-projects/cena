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
package org.adhuc.cena.menu.steps.serenity;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;

/**
 * An abstract service client steps, providing convenient methods for resource resolution and assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
@Slf4j
public abstract class AbstractServiceClientSteps {

    final AuthenticationProvider authenticationProvider = AuthenticationProvider.instance();
    @Delegate
    private ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate(authenticationProvider);
    @Delegate
    private StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    public RequestSpecification rest() {
        return authenticationProvider.rest();
    }

}
