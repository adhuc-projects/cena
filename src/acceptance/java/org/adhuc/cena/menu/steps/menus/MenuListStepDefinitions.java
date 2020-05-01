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

import static java.util.stream.Collectors.toList;

import static org.adhuc.cena.menu.steps.serenity.menus.MenuValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.menus.MenuValue.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cucumber.api.DataTable;
import cucumber.api.Transform;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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
        var menusByOwner = new HashMap<AuthenticationType, List<MenuValue>>();
        transformToOwnedMenus(dataTable).forEach(ownedMenu -> {
            if (!menusByOwner.containsKey(ownedMenu.owner)) {
                menusByOwner.put(ownedMenu.owner, new ArrayList<>());
            }
            menusByOwner.get(ownedMenu.owner).add(ownedMenu.menu);
        });
        menusByOwner.forEach((key, value) -> menuListAssumptions.assumeInMenusListOwnedBy(value, key));
    }

    @Given("^an existing menu from the recipe for (.*)'s (.*)$")
    public void existingMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuListAssumptions.assumeInMenusList(builder().withDate(date).withMealType(mealType)
                .withMainCourseRecipes(recipeList.storedRecipe()).build());
    }

    @Given("^no existing menu$")
    public void noExistingMenu() {
        menuListAssumptions.assumeEmptyMenusList();
    }

    @Given("^no existing menu between (.*) and (.*)$")
    public void noExistingMenu(@Transform(MenuDateTransformer.class) LocalDate since,
                               @Transform(MenuDateTransformer.class) LocalDate until) {
        menuListAssumptions.assumeEmptyMenusList(since, until);
    }

    @Given("^no existing menu for (.*)'s (.*)$")
    public void noExistingMenu(@Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuListAssumptions.assumeNotInMenusList(date, mealType);
    }

    @When("^he lists the menus$")
    public void listMenus() {
        var menus = menuList.getMenus();
        menuList.storeMenus(menus);
    }

    @When("^he lists the menus between (.*) and (.*)$")
    public void listMenus(@Transform(MenuDateTransformer.class) LocalDate since,
                          @Transform(MenuDateTransformer.class) LocalDate until) {
        var menus = menuList.getMenus(since, until);
        menuList.storeMenus(menus);
    }

    @When("^he attempts retrieving list of menus$")
    public void attemptListingMenus() {
        menuList.attemptGetMenus();
    }

    @Then("^the menus list is empty$")
    public void emptyMenuList() {
        menuListAssertions.assertEmptyMenusList(menuList.storedMenus());
    }

    @Then("^the menus list contains the following menus$")
    public void followingMenusFoundInList(DataTable dataTable) {
        var menus = transformToMenus(dataTable);
        menuListAssertions.assertInMenusList(menus, menuList.storedMenus(), COMPARATOR);
    }

    @Then("^the menus list does not contain the following menus$")
    public void followingMenusNotFoundInList(DataTable dataTable) {
        var menus = transformToMenus(dataTable);
        menuListAssertions.assertNotInMenusList(menus, menuList.storedMenus(), COMPARATOR);
    }

    @Then("^the menu can be found in the menus list$")
    public void menuFoundInList() {
        menuListAssertions.assertInMenusList(menuList.storedMenu());
    }

    @Then("^the menu cannot be found in the menus list$")
    public void menuNotFoundInList() {
        menuListAssertions.assertNotInMenusList(menuList.storedMenu());
    }

    private List<OwnedMenu> transformToOwnedMenus(DataTable dataTable) {
        return dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> new OwnedMenu(authenticationTypes.transform(attributes.get(OWNER_ATTRIBUTE)),
                        builder()
                                .withDate(menuDateTransformer.transform(attributes.get(DATE_ATTRIBUTE)))
                                .withMealType(attributes.get(MEAL_TYPE_ATTRIBUTE))
                                .withCovers(Integer.parseInt(attributes.get(COVERS_ATTRIBUTE)))
                                .withMainCourseRecipes(recipeList.storedAssumedRecipe(attributes.get(MAIN_COURSE_RECIPES_ATTRIBUTE)))
                                .build()))
                .collect(toList());
    }

    private List<MenuValue> transformToMenus(DataTable dataTable) {
        return dataTable.asMaps(String.class, String.class).stream()
                .map(attributes -> builder()
                        .withDate(menuDateTransformer.transform(attributes.get(DATE_ATTRIBUTE)))
                        .withMealType(attributes.get(MEAL_TYPE_ATTRIBUTE))
                        .withCovers(Integer.parseInt(attributes.get(COVERS_ATTRIBUTE)))
                        .withMainCourseRecipes(recipeList.storedAssumedRecipe(attributes.get(MAIN_COURSE_RECIPES_ATTRIBUTE)))
                        .build())
                .collect(toList());
    }

    @RequiredArgsConstructor
    @Getter
    @Accessors(fluent = true)
    private static final class OwnedMenu {
        private final AuthenticationType owner;
        private final MenuValue menu;
    }

}
