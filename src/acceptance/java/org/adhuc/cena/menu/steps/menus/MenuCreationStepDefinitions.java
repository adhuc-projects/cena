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

import cucumber.api.Transform;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.menus.MenuCreationSteps;
import org.adhuc.cena.menu.steps.serenity.menus.MenuValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

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

    @When("^he creates a menu from the recipe for (-?\\d+) covers for (.*)'s (\\w+)$")
    public void createMenu(int covers, @Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuCreation.storeMenu(
                menuCreation.createMenu(MenuValue.builder().withDate(date).withMealType(mealType)
                        .withCovers(covers).withMainCourseRecipes(recipeList.storedRecipe()).build()));
    }

    @When("^he creates a menu from \"(.*)\" recipe for (-?\\d+) covers for (.*)'s (\\w+)$")
    public void createMenu(String recipeName, int covers, @Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuCreation.storeMenu(
                menuCreation.createMenu(MenuValue.builder().withDate(date).withMealType(mealType)
                        .withCovers(covers).withMainCourseRecipes(recipeList.storedAssumedRecipe(recipeName))
                        .build()));
    }

    @When("^he creates a menu from the recipes for (-?\\d+) covers for (.*)'s (\\w+)$")
    public void createMenuMultipleRecipes(int covers, @Transform(MenuDateTransformer.class) LocalDate date, String mealType) {
        menuCreation.storeMenu(
                menuCreation.createMenu(MenuValue.builder().withDate(date).withMealType(mealType)
                        .withCovers(covers).withMainCourseRecipes(recipeList.storedAssumedRecipes()).build()));
    }

    @When("^he creates a menu from the recipe without date$")
    public void createMenuWithoutDate() {
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withDate(null)
                .withMainCourseRecipes(recipeList.storedRecipe()).build()));
    }

    @When("^he creates a menu from the recipe without meal type$")
    public void createMenuWithoutMealType() {
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withMealType(null)
                .withMainCourseRecipes(recipeList.storedRecipe()).build()));
    }

    @When("^he creates a menu from the recipe with unknown \"(.*)\" meal type$")
    public void createMenuWithInvalidMealType(String mealType) {
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withMealType(mealType)
                .withMainCourseRecipes(recipeList.storedRecipe()).build()));
    }

    @When("^he creates a menu from the recipe without covers for today's lunch$")
    public void createMenuWithoutCovers() {
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withCovers(null)
                .withMainCourseRecipes(recipeList.storedRecipe()).build()));
    }

    @When("^he creates a menu without main course recipe for today's lunch$")
    public void createMenuWithoutMainCourseRecipes() {
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withNoMainCourseRecipes().build()));
    }

    @When("^he creates a menu from unknown recipe for today's lunch$")
    public void createMenuWithUnknownMainCourseRecipe() {
        var recipe = recipeList.storeRecipe(RecipeValue.buildUnknownRecipeValue(recipeList.recipesResourceUrl()));
        menuCreation.storeMenu(menuCreation.createMenu(MenuValue.builder().withMainCourseRecipes(recipe).build()));
    }

    @When("^he tries to create a menu from the recipe for 2 covers for today's lunch$")
    public void tryToCreateMenu() {
        menuCreation.tryToCreateMenu(MenuValue.builder().withMainCourseRecipes(recipeList.storedRecipe()).build());
    }

    @Then("^the menu is created$")
    public void menuCreated() {
        menuCreation.assertMenuSuccessfullyCreated(menuCreation.storedMenu());
    }

    @Then("^an error notifies that menu must have a date$")
    public void errorOnMenuCreationWithoutDate() {
        menuCreation.assertInvalidRequestConcerningMissingBodyField(MenuValue.DATE_FIELD);
    }

    @Then("^an error notifies that menu must have a meal type$")
    public void errorOnMenuCreationWithoutMealType() {
        menuCreation.assertInvalidRequestConcerningMissingBodyField(MenuValue.MEAL_TYPE_FIELD);
    }

    @Then("^an error notifies that menu cannot be created with unknown \"(.*)\" meal type$")
    public void errorOnIngredientCreationWithUnknownMeasurementType(String mealType) {
        menuCreation.assertInvalidRequestConcerningUnknownMealUnit(mealType);
    }

    @Then("^an error notifies that menu must have covers$")
    public void errorOnMenuCreationWithoutCovers() {
        menuCreation.assertInvalidRequestConcerningMissingBodyField(MenuValue.COVERS_FIELD);
    }

    @Then("^an error notifies that menu cannot be created with invalid (-?\\d+) covers$")
    public void errorOnIngredientCreationWithInvalidCovers(int covers) {
        menuCreation.assertInvalidRequestConcerningInvalidCovers(covers);
    }

    @Then("^an error notifies that menu must have main course recipes$")
    public void errorOnMenuCreationWithoutMainCourseRecipes() {
        menuCreation.assertInvalidRequestConcerningMissingBodyField(MenuValue.MAIN_COURSE_RECIPES_FIELD);
    }

    @Then("^an error notifies that menu cannot be linked to unknown recipe$")
    public void errorOnMenuCreationWithUnknownRecipe() {
        menuCreation.assertMenuCannotBeCreatedWithUnknownRecipe(menuCreation.storedMenu(), recipeList.storedRecipe());
    }

    @Then("^an error notifies that menu already exists$")
    public void errorOnMenuCreationAlreadyExisting() {
        menuCreation.assertMenuAlreadyExists(menuCreation.storedMenu());
    }

}
