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
import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.COMPARATOR;

import java.util.Optional;

import lombok.NonNull;
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
public class RecipeIngredientListServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();

    @Steps
    private RecipeIngredientAdditionServiceClientSteps recipeIngredientAdditionServiceClient;

    @Step("Assume ingredient {0} is in recipe {1} ingredients list")
    public void assumeIngredientRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        if (getFromRecipeIngredientsList(ingredient, recipe).isEmpty()) {
            recipeIngredientAdditionServiceClient.addIngredientToRecipe(ingredient, recipe);
        }
        assumeThat(getFromRecipeIngredientsList(ingredient, recipe)).isPresent();
    }

    @Step("Assume ingredient {0} is not in recipe {1} ingredients list")
    public void assumeIngredientNotRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        // TODO delete relation if existing
        assumeThat(getFromRecipeIngredientsList(ingredient, recipe)).isEmpty();
    }

    @Step("Assert ingredient {0} is in recipe {1} ingredients list")
    public void assertIngredientRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        assertThat(getFromRecipeIngredientsList(ingredient, recipe)).isPresent().get()
                .usingComparator(COMPARATOR).isEqualTo(new RecipeIngredientValue(ingredient));
    }

    /**
     * Gets the recipe ingredient corresponding to the specified one from the recipe's ingredients list if existing,
     * based on its identity.
     * The recipe's ingredients list is retrieved from the server. If recipe ingredient is retrieved from server, it
     * should be populated with all attributes, especially its identity and links.
     *
     * @param recipe the recipe.
     * @param ingredient the ingredient related to recipe.
     * @return the recipe ingredient retrieved from list.
     */
    private Optional<RecipeIngredientValue> getFromRecipeIngredientsList(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        var response = rest().get(recipe.getIngredients()).then();
        return Optional.ofNullable(statusAssertionDelegate.assertOk(response)
                .extract().jsonPath().param("id", ingredient.id())
                .getObject("_embedded.data.find { ingredient->ingredient.id == id }", RecipeIngredientValue.class));
    }

}
