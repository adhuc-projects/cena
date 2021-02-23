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
package org.adhuc.cena.menu.steps.authentication;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.AuthenticationSteps;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

import java.util.Map;

/**
 * The authentication steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
public class AuthenticationStepDefinitions {

    @Steps
    private AuthenticationSteps authentication;

    @Given("a community user")
    public void communityUser() {
        authentication.withCommunityUser();
    }

    @Given("an authenticated {user}")
    public void authenticated(AuthenticationType authenticationType) {
        authentication.withAuthentication(authenticationType);
    }

    @Then("an error notifies that user is not authenticated")
    public void errorUserNotAuthenticated() {
        authentication.assertUserNotAuthenticated();
    }

    @Then("an error notifies that user is not authorized")
    public void errorUserNotAuthorized() {
        authentication.assertUserNotAuthorized();
    }

}
