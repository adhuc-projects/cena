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

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.menus.MenuCreationSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;

/**
 * The menu creation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@StepDefAnnotation
public class MenuCreationStepDefinitions {

    @Steps
    private RecipeListSteps recipeList;
    @Steps
    private MenuCreationSteps menuCreation;

    @When("^he creates a menu from the recipe for (\\d+) covers for today's (\\w+)$")
    public void createMenu(int covers, String mealType) {
        menuCreation.storeMenu(
                menuCreation.createMenu(new MenuValue(LocalDate.now(), mealType, covers, recipeList.storedRecipe())));
    }

    @When("^he tries to create a menu from the recipe for 2 covers for today's lunch$")
    public void tryToCreateMenu() {
        menuCreation.tryToCreateMenu(new MenuValue(LocalDate.now(), "lunch", 2, recipeList.storedRecipe()));
    }

    @Then("^the menu is created$")
    public void menuCreated() {
        menuCreation.assertMenuSuccessfullyCreated(menuCreation.storedMenu());
    }

}
