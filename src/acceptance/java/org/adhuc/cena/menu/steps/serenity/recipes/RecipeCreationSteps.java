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
package org.adhuc.cena.menu.steps.serenity.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;

import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.DEFAULT_NAME;
import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.DEFAULT_SERVINGS;
import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.AUTHENTICATED_USER;

import java.util.function.Supplier;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * The recipe creation rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeCreationSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Steps
    private RecipeDetailSteps recipeDetail;

    @Step("Create the recipe {0}")
    public RecipeValue createRecipe(RecipeValue recipe) {
        return createRecipe(recipe, this::rest);
    }

    @Step("Create the recipe {0} without number of servings")
    public RecipeValue createRecipeWithoutServings(RecipeValue recipe) {
        return createRecipe(recipe.withoutServings(), this::rest);
    }

    @Step("Create the recipe {0} as authenticated user")
    public RecipeValue createRecipeAsAuthenticatedUser(RecipeValue recipe) {
        return createRecipeAs(recipe, AUTHENTICATED_USER);
    }

    @Step("Create the recipe {0} as {1}")
    public RecipeValue createRecipeAs(RecipeValue recipe, AuthenticationType authenticationType) {
        return createRecipe(recipe, () -> rest(authenticationType));
    }

    private RecipeValue createRecipe(RecipeValue recipe, Supplier<RequestSpecification> specificationSupplier) {
        var recipesResourceUrl = recipesResourceUrl();
        specificationSupplier.get().contentType(HAL_JSON_VALUE)
                .body(recipe.withoutId())
                .post(recipesResourceUrl).andReturn();
        return recipe;
    }

    @Step("Create a recipe without name")
    public RecipeValue createRecipeWithoutName() {
        return createRecipe(new RecipeValue(null));
    }

    @Step("Create a recipe without content")
    public RecipeValue createRecipeWithoutContent() {
        return createRecipe(new RecipeValue(DEFAULT_NAME, null, DEFAULT_SERVINGS));
    }

    @Step("Assert recipe {0} has been successfully created")
    public void assertRecipeSuccessfullyCreated(RecipeValue recipe) {
        var recipeLocation = assertCreated().extract().header(LOCATION);
        assertThat(recipeLocation).isNotBlank();
        var retrievedRecipe = recipeDetail.getRecipeFromUrl(recipeLocation);
        retrievedRecipe.assertEqualTo(recipe);
        assertThat(retrievedRecipe.author()).isEqualTo(AuthenticationProvider.instance().currentlyAuthenticatedUser());
    }

}
