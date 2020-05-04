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

import org.adhuc.cena.menu.steps.serenity.menus.MenuDeletionSteps;

/**
 * The menu deletion steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@StepDefAnnotation
public class MenuDeletionStepDefinitions {

    @Steps
    private MenuDeletionSteps menuDeletion;

    @When("^he attempts deleting the menu scheduled for (.*)'s (\\w+)$")
    public void attemptDeletingMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuDeletion.attemptDeletingMenu(date, mealType);
    }

    @When("^he deletes the menu scheduled for (.*)'s (\\w+)$")
    public void deleteMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuDeletion.storeMenu(menuDeletion.deleteMenu(date, mealType));
    }

    @Then("^the menu is deleted$")
    public void menuDeleted() {
        menuDeletion.assertMenuSuccessfullyDeleted();
    }

}
