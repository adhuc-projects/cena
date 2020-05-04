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
package org.adhuc.cena.menu.steps.management;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ManagementSteps;

/**
 * The health steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.0.1
 */
@StepDefAnnotation
public class HealthStepDefinitions {

    @Steps
    private ManagementSteps management;

    @Given("^a running service$")
    public void runningService() {
        // nothing to do here
    }

    @When("^I check the service health$")
    public void checkServiceHealth() {
        management.callHealthCheckService();
    }

    @Then("^the service health is ok$")
    public void serviceHealthIsOk() {
        management.assertResponseIsOk();
    }

    @Then("^the health detail is not available$")
    public void healthDetailIsNotAvailable() {
        management.assertDetailIsNotAvailable();
    }

    @Then("^the health detail is available$")
    public void healthDetailIsAvailable() {
        management.assertDiskUsageIsAvailable();
    }

}
