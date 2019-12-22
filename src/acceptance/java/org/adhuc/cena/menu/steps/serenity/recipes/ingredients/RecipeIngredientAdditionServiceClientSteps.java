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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;

import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.SUPER_ADMINISTRATOR;

import java.util.function.Supplier;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeStorageDelegate;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The recipe's ingredients addition rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeIngredientAdditionServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Steps
    private RecipeIngredientDetailServiceClientSteps recipeIngredientDetailServiceClient;

    @Step("Add ingredient {0} to recipe {1}")
    public void addIngredientToRecipe(IngredientValue ingredient, RecipeValue recipe) {
        addIngredientToRecipe(ingredient, recipe, this::rest);
    }

    @Step("Add ingredient {0} to recipe {1} as super administrator")
    public void addIngredientToRecipeAsSuperAdministrator(IngredientValue ingredient, RecipeValue recipe) {
        addIngredientToRecipe(ingredient, recipe, () -> rest(SUPER_ADMINISTRATOR));
    }

    private void addIngredientToRecipe(IngredientValue ingredient, RecipeValue recipe,
                                       Supplier<RequestSpecification> specificationSupplier) {
        specificationSupplier.get().contentType(HAL_JSON_VALUE).body(new RecipeIngredientValue(ingredient))
                .post(recipe.getIngredients()).andReturn();
    }

    @Step("Add ingredient without id to recipe {0}")
    public void addIngredientWithoutIdToRecipe(RecipeValue recipe) {
        rest().contentType(HAL_JSON_VALUE).body(new RecipeIngredientValue(null)).post(recipe.getIngredients());
    }

    @Step("Assert ingredient {0} has been successfully added to recipe {1}")
    public void assertIngredientSuccessfullyAddedToRecipe(IngredientValue ingredient, RecipeValue recipe) {
        var recipeIngredientLocation = assertCreated().extract().header(LOCATION);
        assertThat(recipeIngredientLocation).isNotBlank().contains(recipe.id()).endsWith(ingredient.id());
        var retrievedRecipeIngredient = recipeIngredientDetailServiceClient.getRecipeIngredientFromUrl(recipeIngredientLocation);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(retrievedRecipeIngredient).usingComparator(RecipeIngredientValue.COMPARATOR)
                    .isEqualTo(new RecipeIngredientValue(ingredient));
            softAssertions.assertThat(retrievedRecipeIngredient.getRecipe()).isEqualTo(recipe.selfLink());
        });
    }

}
