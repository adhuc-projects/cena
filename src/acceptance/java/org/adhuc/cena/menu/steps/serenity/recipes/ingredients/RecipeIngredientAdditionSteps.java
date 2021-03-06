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
package org.adhuc.cena.menu.steps.serenity.recipes.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;

import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.DEFAULT_MEASUREMENT_UNIT;
import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.DEFAULT_QUANTITY;
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
 * @version 0.3.0
 * @since 0.2.0
 */
public class RecipeIngredientAdditionSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();
    @Delegate
    private final RecipeIngredientStorageDelegate recipeIngredientStorageDelegate = new RecipeIngredientStorageDelegate();

    @Steps
    private RecipeIngredientDetailSteps recipeIngredientDetail;

    @Step("Add ingredient {0} to recipe {1} with quantity as {2} {3}")
    public RecipeIngredientValue addIngredientToRecipe(IngredientValue ingredient, RecipeValue recipe, int quantity, String measurementUnit) {
        return addIngredientToRecipe(ingredient, recipe, false, quantity, measurementUnit);
    }

    @Step("Add main ingredient {0} to recipe {1} with quantity as {2} {3}")
    public RecipeIngredientValue addMainIngredientToRecipe(IngredientValue ingredient, RecipeValue recipe, int quantity, String measurementUnit) {
        return addIngredientToRecipe(ingredient, recipe, true, quantity, measurementUnit);
    }

    private RecipeIngredientValue addIngredientToRecipe(IngredientValue ingredient, RecipeValue recipe,
                                                        boolean mainIngredient, int quantity, String measurementUnit) {
        return addIngredientToRecipe(new RecipeIngredientValue(ingredient, mainIngredient, quantity, measurementUnit), recipe, this::rest);
    }

    @Step("Add ingredient {0} to recipe {1} without specifying whether ingredient is a main ingredient")
    public RecipeIngredientValue addIngredientToRecipeWithoutMain(IngredientValue ingredient, RecipeValue recipe) {
        return addIngredientToRecipe(new RecipeIngredientValue(ingredient, null, DEFAULT_QUANTITY, DEFAULT_MEASUREMENT_UNIT),
                recipe, this::rest);
    }

    @Step("Add ingredient {0} to recipe {1}")
    public RecipeIngredientValue addIngredientToRecipeWithoutQuantity(IngredientValue ingredient, RecipeValue recipe) {
        return addIngredientToRecipe(new RecipeIngredientValue(ingredient), recipe, this::rest);
    }

    @Step("Add ingredient {0} to recipe {1} as super administrator")
    public RecipeIngredientValue addIngredientToRecipeAsSuperAdministrator(RecipeIngredientValue recipeIngredient, RecipeValue recipe) {
        return addIngredientToRecipe(recipeIngredient, recipe, () -> rest(SUPER_ADMINISTRATOR));
    }

    private RecipeIngredientValue addIngredientToRecipe(RecipeIngredientValue recipeIngredient, RecipeValue recipe,
                                                        Supplier<RequestSpecification> specificationSupplier) {
        specificationSupplier.get().contentType(HAL_JSON_VALUE).body(recipeIngredient)
                .post(recipe.getIngredients()).andReturn();
        return recipeIngredient;
    }

    @Step("Add ingredient without id to recipe {0}")
    public void addIngredientWithoutIdToRecipe(RecipeValue recipe) {
        rest().contentType(HAL_JSON_VALUE).body(new RecipeIngredientValue(null)).post(recipe.getIngredients());
    }

    @Step("Assert ingredient {0} has been successfully added to recipe {1}")
    public RecipeIngredientValue assertIngredientSuccessfullyAddedToRecipe(IngredientValue ingredient, RecipeValue recipe) {
        var recipeIngredientLocation = assertCreated().extract().header(LOCATION);
        assertThat(recipeIngredientLocation).isNotBlank().contains(recipe.id()).endsWith(ingredient.id());
        var retrievedRecipeIngredient = recipeIngredientDetail.getRecipeIngredientFromUrl(recipeIngredientLocation);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(retrievedRecipeIngredient).usingComparator(RecipeIngredientValue.COMPARATOR)
                    .isEqualTo(new RecipeIngredientValue(ingredient));
            softAssertions.assertThat(retrievedRecipeIngredient.getRecipe()).isEqualTo(recipe.selfLink());
        });
        return retrievedRecipeIngredient;
    }

    @Step("Assert ingredient name {0} already exists")
    public void assertNonCorrespondingMeasurementUnit(IngredientValue ingredient, RecipeValue recipe,
                                                      RecipeIngredientValue recipeIngredient) {
        var jsonPath = assertBadRequest().extract().jsonPath();
        assertSoftly(softly -> {
            softly.assertThat(jsonPath.getString("message")).contains("Unable to add ingredient '" + ingredient.id() +
                    "' to recipe '" + recipe.id() + "': measurement unit " + recipeIngredient.measurementUnit() +
                    " does not correspond to ingredient's measurement types " + ingredient.measurementTypes());
            softly.assertThat(jsonPath.getInt("code")).isEqualTo(900001);
        });
    }

}
