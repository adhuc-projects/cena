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
package org.adhuc.cena.menu.steps.serenity.recipes.ingredients;

import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.SUPER_ADMINISTRATOR;

import java.util.function.Supplier;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import org.assertj.core.api.SoftAssertions;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipe's ingredients removal rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeIngredientRemovalSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final RecipeIngredientListClientDelegate listClient = new RecipeIngredientListClientDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Step("Remove ingredients from recipe {0}")
    public void removeIngredientsFromRecipe(RecipeValue recipe) {
        rest().delete(recipe.getIngredients()).andReturn();
    }

    @Step("Remove ingredients from recipe {0} as super administrator")
    public void removeIngredientsFromRecipeAsSuperAdministrator(RecipeValue recipe) {
        rest(SUPER_ADMINISTRATOR).delete(recipe.getIngredients()).andReturn();
    }

    @Step("Remove ingredient {0} from recipe {1}")
    public void removeIngredientFromRecipe(IngredientValue ingredient, RecipeValue recipe) {
        removeIngredientFromRecipe(ingredient, recipe, this::rest);
    }

    @Step("Remove ingredient {0} from recipe {1} as super administrator")
    public void removeIngredientFromRecipeAsSuperAdministrator(IngredientValue ingredient, RecipeValue recipe) {
        removeIngredientFromRecipe(ingredient, recipe, () -> rest(SUPER_ADMINISTRATOR));
    }

    private void removeIngredientFromRecipe(IngredientValue ingredient, RecipeValue recipe,
                                            Supplier<RequestSpecification> specificationSupplier) {
        var recipeIngredient = listClient.getFromRecipeIngredientsList(ingredient, recipe);
        assumeThat(recipeIngredient).isPresent();
        specificationSupplier.get().delete(recipeIngredient.get().selfLink()).andReturn();
    }

    @Step("Attempt removing ingredient {0} from recipe {1}")
    public void attemptRemoveIngredientFromRecipe(IngredientValue ingredient, RecipeValue recipe) {
        var recipeIngredient = RecipeIngredientValue.buildUnknownRecipeIngredientValue(ingredient, recipe);
        rest().delete(recipeIngredient.selfLink()).andReturn();
    }

    @Step("Assert ingredients has been successfully removed from recipe {0}")
    public void assertIngredientsSuccessfullyRemovedFromRecipe(RecipeValue recipe) {
        assertNoContent();
    }

    @Step("Assert ingredient {0} has been successfully removed from recipe {1}")
    public void assertIngredientSuccessfullyRemovedFromRecipe(IngredientValue ingredient, RecipeValue recipe) {
        assertNoContent();
    }

    @Step("Assert ingredient {0} cannot be removed from recipe {1}")
    public void assertIngredientNotRemovableFromRecipe(IngredientValue ingredient, RecipeValue recipe) {
        var jsonPath = assertNotFound().extract().jsonPath();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(jsonPath.getString("message")).contains("Ingredient '" + ingredient.id() +
                    "' is not related to recipe '" + recipe.id() + "'");
            softly.assertThat(jsonPath.getInt("code")).isEqualTo(900101);
        });
    }

}
