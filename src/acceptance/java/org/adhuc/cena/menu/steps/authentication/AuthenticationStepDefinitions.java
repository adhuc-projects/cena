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

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.AuthenticationServiceClientSteps;

/**
 * The authentication steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.0.1
 */
@StepDefAnnotation
public class AuthenticationStepDefinitions {

    @Steps
    private AuthenticationServiceClientSteps authenticationServiceClient;

    @Given("^a community user$")
    public void communityUser() {
        authenticationServiceClient.withCommunityUser();
    }

    @Given("^an authenticated ingredient manager$")
    public void authenticatedIngredientManager() {
        authenticationServiceClient.withIngredientManager();
    }

    @Given("^an authenticated actuator manager$")
    public void authenticatedActuatorManager() {
        authenticationServiceClient.withActuatorManager();
    }

    @Given("^an authenticated super administrator$")
    public void authenticatedSuperAdministrator() {
        authenticationServiceClient.withSuperAdministrator();
    }

    @Then("^an error notifies that user is not authenticated$")
    public void errorUserNotAuthenticated() {
        authenticationServiceClient.assertUserNotAuthenticated();
    }

    @Then("^an error notifies that user is not authorized$")
    public void errorUserNotAuthorized() {
        authenticationServiceClient.assertUserNotAuthorized();
    }

}
