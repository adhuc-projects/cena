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
package org.adhuc.cena.menu.steps.menus;

import java.time.LocalDate;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.menus.MenuListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuListAssumptionsSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuListSteps;

/**
 * The menus list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@StepDefAnnotation
public class MenuListStepDefinitions {

    @Steps
    private MenuListSteps menuList;
    @Steps
    private MenuListAssumptionsSteps menuListAssumptions;
    @Steps
    private MenuListAssertionsSteps menuListAssertions;

    @Given("^no existing menu for today's (.*)$")
    public void noExistingMenu(String mealType) {
        menuListAssumptions.assumeNotInMenusList(LocalDate.now(), mealType);
    }

    @Then("^the menu can be found in the menus list starting from today$")
    public void menuFoundInList() {
        menuListAssertions.assertInMenusList(menuList.storedMenu());
    }

    @Then("^the menu cannot be found in the menus list starting from today$")
    public void menuNotFoundInList() {
        menuListAssertions.assertNotInMenusList(menuList.storedMenu());
    }

}
