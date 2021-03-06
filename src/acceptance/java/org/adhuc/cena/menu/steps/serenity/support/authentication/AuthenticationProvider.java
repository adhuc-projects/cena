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
package org.adhuc.cena.menu.steps.serenity.support.authentication;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.*;

import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import net.serenitybdd.rest.SerenityRest;

/**
 * Provides access to a {@link RequestSpecification} to call rest-services, managing the authentication process, and
 * abstracts the authentication definition.
 * <p>
 * This provider also manages the initialization of the underlying {@link SerenityRest#rest()}'s
 * {@link RequestSpecification}, to ensure that only one is created for each new test execution, and is used for each
 * call to {@link #rest()}.
 * <p>
 * It is still possible to execute requests with specific authentication, by-passing the general authentication for the
 * test execution. This can be useful to ensure assuming steps are run with appropriate authentication level without
 * impacting the rest of the test execution. Refer to {@link #restWithAuth(AuthenticationType)} for this specific usage.
 * <p>
 * Must be used as a singleton, using {@link #instance()} to ensure that it is shared between all steps.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
public class AuthenticationProvider {

    private static AuthenticationProvider INSTANCE = new AuthenticationProvider();

    private AuthenticationType authenticationType;
    private Authentication authentication;

    public static AuthenticationProvider instance() {
        return INSTANCE;
    }

    private AuthenticationProvider() {
        clean();
    }

    /**
     * Cleans the currently defined authentication.
     */
    public void clean() {
        authenticate(COMMUNITY_USER);
    }

    /**
     * Provides a {@link RequestSpecification} enhanced with potential authentication, based on previously specified
     * authentication type.
     *
     * @return the {@link RequestSpecification} to execute request.
     * @see #withAuthentication(AuthenticationType)
     */
    public RequestSpecification rest() {
        return authentication.authenticate(SerenityRest.rest());
    }

    /**
     * Provides a {@link RequestSpecification} enhanced with potential authentication as requested.
     *
     * @param authenticationType the authentication type.
     * @return the {@link RequestSpecification} to execute request.
     */
    public RequestSpecification restWithAuth(AuthenticationType authenticationType) {
        return authentication(authenticationType).authenticate(SerenityRest.rest());
    }

    /**
     * Indicates whether a user is currently authenticated.
     *
     * @return {@code true} if a user has been authenticated, {@code false} otherwise.
     */
    public boolean isAuthenticated() {
        return currentlyAuthenticatedUser() != null;
    }

    /**
     * Gets the current authentication, as set through {@link #withAuthentication(AuthenticationType)}.
     *
     * @return the current authentication.
     */
    public AuthenticationType currentAuthentication() {
        return authenticationType;
    }

    /**
     * Gets the currently authenticated user, or {@code null} if none is authenticated.
     *
     * @return the currently authenticated user, set using {@link #withAuthentication(AuthenticationType)}.
     */
    public String currentlyAuthenticatedUser() {
        return authentication.getUser();
    }

    /**
     * Authenticates with the specified authentication type.
     *
     * @param authenticationType the authentication type.
     */
    public void withAuthentication(@NonNull AuthenticationType authenticationType) {
        authenticate(authenticationType);
    }

    private void authenticate(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
        authentication = authentication(authenticationType);
    }

    private Authentication authentication(AuthenticationType authenticationType) {
        return authenticationType.getAuthenticationSupplier().get();
    }
}
