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

import org.adhuc.cena.menu.steps.serenity.AuthenticationSteps;

/**
 * The authentication steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.0.1
 */
@StepDefAnnotation
public class AuthenticationStepDefinitions {

    @Steps
    private AuthenticationSteps authentication;

    @Given("^a community user$")
    public void communityUser() {
        authentication.withCommunityUser();
    }

    @Given("^an authenticated user$")
    public void authenticatedUser() {
        authentication.withAuthenticatedUser();
    }

    @Given("^an authenticated ingredient manager$")
    public void authenticatedIngredientManager() {
        authentication.withIngredientManager();
    }

    @Given("^an authenticated actuator manager$")
    public void authenticatedActuatorManager() {
        authentication.withActuatorManager();
    }

    @Given("^an authenticated super administrator$")
    public void authenticatedSuperAdministrator() {
        authentication.withSuperAdministrator();
    }

    @Then("^an error notifies that user is not authenticated$")
    public void errorUserNotAuthenticated() {
        authentication.assertUserNotAuthenticated();
    }

    @Then("^an error notifies that user is not authorized$")
    public void errorUserNotAuthorized() {
        authentication.assertUserNotAuthorized();
    }

}
