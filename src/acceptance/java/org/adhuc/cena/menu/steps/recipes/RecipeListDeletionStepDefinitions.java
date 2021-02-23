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

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeDeletionSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListAssertionsSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;

/**
 * The recipes list deletion steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeListDeletionStepDefinitions {

    @Steps
    private RecipeDeletionSteps recipeDeletion;
    @Steps
    private RecipeListSteps recipeList;
    @Steps
    private RecipeListAssertionsSteps recipeListAssertions;

    @When("he deletes the recipes")
    public void deleteRecipes() {
        recipeDeletion.deleteRecipes();
    }

    @Then("the recipes have been deleted")
    public void recipesDeleted() {
        recipeDeletion.assertNoContent();
    }

    @Then("no recipe is left in the recipes list")
    public void noRecipesLeftInList() {
        recipeListAssertions.assertEmptyRecipesList();
    }

    @Then("no existing recipe has been deleted")
    public void noExistingRecipeDeleted() {
        recipeListAssertions.assertInRecipesList(recipeList.storedAssumedRecipes(), recipeList.getRecipes());
    }

}
