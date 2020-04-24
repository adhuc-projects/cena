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

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.*;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * The authentication rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
public class AuthenticationSteps {

    private final AuthenticationProvider authenticationProvider = AuthenticationProvider.instance();

    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @Step("Defines a community user")
    public void withCommunityUser() {
        authenticationProvider.withAuthentication(COMMUNITY_USER);
    }

    @Step("Authenticate as {}")
    public void withAuthentication(AuthenticationType authenticationType) {
        authenticationProvider.withAuthentication(authenticationType);
    }

    @Step("Assert user is not authenticated")
    public void assertUserNotAuthenticated() {
        assertUnauthorized();
    }

    @Step("Assert user is not authorized")
    public void assertUserNotAuthorized() {
        assertForbidden();
    }

}
