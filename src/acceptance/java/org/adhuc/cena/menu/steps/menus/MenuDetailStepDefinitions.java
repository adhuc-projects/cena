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
package org.adhuc.cena.menu.steps.menus;

import java.time.LocalDate;

import cucumber.api.Transform;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.menus.MenuDetailSteps;

/**
 * The menu details steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@StepDefAnnotation
public class MenuDetailStepDefinitions {

    @Steps
    private MenuDetailSteps menuDetail;

    @When("^he retrieves the menu scheduled for (.*)'s (\\w+)$")
    public void retrieveMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuDetail.storeMenu(menuDetail.retrieveMenu(date, mealType));
    }

    @When("^he attempts retrieving the menu scheduled for (.*)'s (\\w+)$")
    public void attemptRetrievingMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuDetail.attemptRetrievingMenu(date, mealType);
    }

    @Then("^the menu details are accessible$")
    public void accessibleMenuDetails() {
        menuDetail.assertMenuInfoIsAccessible(menuDetail.storedMenu());
    }

    @Then("^an error notifies that menu has not been found$")
    public void errorNotFoundMenu() {
        menuDetail.assertNotFound();
    }

}
