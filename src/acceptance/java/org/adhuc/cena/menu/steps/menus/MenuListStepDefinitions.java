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

import static org.adhuc.cena.menu.steps.serenity.menus.MenuValue.builder;

import java.time.LocalDate;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.StepDefAnnotation;
import lombok.RequiredArgsConstructor;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.authentication.AuthenticationTypeTransformer;
import org.adhuc.cena.menu.steps.serenity.menus.MenuListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuListAssumptionsSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuListSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * The menus list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@StepDefAnnotation
public class MenuListStepDefinitions {

    private static final String OWNER_ATTRIBUTE = "owner";
    private static final String DATE_ATTRIBUTE = "date";
    private static final String MEAL_TYPE_ATTRIBUTE = "mealType";
    private static final String COVERS_ATTRIBUTE = "covers";
    private static final String MAIN_COURSE_RECIPES_ATTRIBUTE = "mainCourseRecipes";

    private static final AuthenticationTypeTransformer authenticationTypes = new AuthenticationTypeTransformer();
    private static final MenuDateTransformer menuDateTransformer = new MenuDateTransformer();

    @Steps
    private MenuListSteps menuList;
    @Steps
    private MenuListAssumptionsSteps menuListAssumptions;
    @Steps
    private MenuListAssertionsSteps menuListAssertions;
    @Steps
    private RecipeListSteps recipeList;

    @Given("^the following existing menus$")
    public void existingMenus(DataTable dataTable) {
        dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new OwnedMenu(authenticationTypes.transform(attributes.get(OWNER_ATTRIBUTE)),
                        builder()
                        .withDate(menuDateTransformer.transform(attributes.get(DATE_ATTRIBUTE)))
                        .withMealType(attributes.get(MEAL_TYPE_ATTRIBUTE))
                        .withCovers(Integer.parseInt(attributes.get(COVERS_ATTRIBUTE)))
                        .withMainCourseRecipes(recipeList.storedAssumedRecipe(attributes.get(MAIN_COURSE_RECIPES_ATTRIBUTE)))
                        .build()))
                .forEach(ownedMenu -> menuListAssumptions.assumeInMenusListOwnedBy(ownedMenu.menu, ownedMenu.owner));
    }

    @Given("^an existing menu from the recipe for today's (.*)$")
    public void existingMenu(String mealType) {
        menuListAssumptions.assumeInMenusList(builder().withDate(LocalDate.now()).withMealType(mealType)
                .withMainCourseRecipes(recipeList.storedRecipe()).build());
    }

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

    @RequiredArgsConstructor
    private static final class OwnedMenu {
        private final AuthenticationType owner;
        private final MenuValue menu;
    }

}
