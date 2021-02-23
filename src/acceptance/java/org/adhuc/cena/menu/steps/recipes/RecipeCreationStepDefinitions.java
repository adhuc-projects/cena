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
package org.adhuc.cena.menu.steps.recipes;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeCreationSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

/**
 * The recipe creation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeCreationStepDefinitions {

    @Steps
    private RecipeCreationSteps recipeCreation;

    @When("he creates the recipe")
    public void createRecipe() {
        recipeCreation.createRecipe(recipeCreation.storedRecipe());
    }

    @When("he creates the recipe without number of servings")
    public void createRecipeWithoutServings() {
        recipeCreation.storeRecipe(recipeCreation.createRecipeWithoutServings(recipeCreation.storedRecipe()));
    }

    @When("he creates the recipe without course types")
    public void createRecipeWithoutCourseTypes() {
        recipeCreation.storeRecipe(recipeCreation.createRecipeWithoutCourseTypes(recipeCreation.storedRecipe()));
    }

    @When("he creates the recipe with the following course types")
    public void createRecipeWithCourseTypes(DataTable dataTable) {
        var courseTypes = dataTable.asList();
        var recipe = recipeCreation.storeRecipe(recipeCreation.storedRecipe().withCourseTypes(courseTypes));
        recipeCreation.createRecipe(recipe);
    }

    @When("he creates a recipe without name")
    public void createRecipeWithoutName() {
        recipeCreation.storeRecipe(recipeCreation.createRecipeWithoutName());
    }

    @When("he creates a recipe without content")
    public void createRecipeWithoutContent() {
        recipeCreation.storeRecipe(recipeCreation.createRecipeWithoutContent());
    }

    @Then("the recipe is created")
    public void recipeCreated() {
        recipeCreation.assertRecipeSuccessfullyCreated(recipeCreation.storedRecipe());
    }

    @Then("an error notifies that recipe must have a name")
    public void errorOnRecipeCreationWithoutName() {
        recipeCreation.assertInvalidRequestConcerningMissingBodyField(RecipeValue.NAME_FIELD);
    }

    @Then("an error notifies that recipe must have a content")
    public void errorOnRecipeCreationWithoutContent() {
        recipeCreation.assertInvalidRequestConcerningMissingBodyField(RecipeValue.CONTENT_FIELD);
    }

    @Then("an error notifies that recipe cannot be created with unknown {string} course type")
    public void errorOnRecipeCreationWithUnknownCourseType(String unknownCourseType) {
        int position = recipeCreation.storedRecipe().courseTypes().indexOf(unknownCourseType);
        recipeCreation.assertInvalidRequestConcerningUnknownCourseType(position, unknownCourseType);
    }

}
