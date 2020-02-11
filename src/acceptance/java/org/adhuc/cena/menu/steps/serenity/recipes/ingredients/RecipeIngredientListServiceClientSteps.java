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

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.ID_AND_QUANTITY_COMPARATOR;

import java.util.Collection;
import java.util.List;

import lombok.NonNull;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListServiceClientSteps;
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
    private final RecipeIngredientListClientDelegate listClient = new RecipeIngredientListClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();
    @Delegate
    private final RecipeStorageDelegate recipeStorage = new RecipeStorageDelegate();
    @Delegate
    private final RecipeIngredientStorageDelegate recipeIngredientStorageDelegate = new RecipeIngredientStorageDelegate();

    @Steps
    private IngredientListServiceClientSteps ingredientListServiceClientSteps;
    @Steps
    private RecipeIngredientAdditionServiceClientSteps recipeIngredientAdditionServiceClient;
    @Steps
    private RecipeIngredientRemovalServiceClientSteps recipeIngredientRemovalServiceClient;

    @Step("Assume recipe {0} contains no ingredient")
    public void assumeRecipeContainsNoIngredient(@NonNull RecipeValue recipe) {
        recipeIngredientRemovalServiceClient.removeIngredientsFromRecipeAsSuperAdministrator(recipe);
        assumeThat(fetchRecipeIngredients(recipe)).isEmpty();
    }

    @Step("Assert empty recipe {0} ingredients list")
    public void assertEmptyRecipeIngredientList(RecipeValue recipe) {
        assertThat(fetchRecipeIngredients(recipe)).isEmpty();
    }

    @Step("Assert empty recipe ingredients list {0}")
    public void assertEmptyRecipeIngredientList(Collection<RecipeIngredientValue> recipeIngredients) {
        assertThat(recipeIngredients).isEmpty();
    }

    @Step("Assume ingredient {0} is in recipe {1} ingredients list")
    public RecipeIngredientValue assumeIngredientRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        if (getFromRecipeIngredientsList(ingredient, recipe).isEmpty()) {
            recipeIngredientAdditionServiceClient.addIngredientToRecipeAsSuperAdministrator(ingredient, recipe);
        }
        var recipeIngredient = getFromRecipeIngredientsList(ingredient, recipe);
        assumeThat(recipeIngredient).isPresent();
        return recipeIngredient.get();
    }

    @Step("Assume ingredients {0} are in recipe {1} ingredients list")
    public Collection<RecipeIngredientValue> assumeIngredientsRelatedToRecipe(List<IngredientValue> ingredients, RecipeValue recipe) {
        var existingIngredients = ingredients.stream()
                .map(i -> ingredientListServiceClientSteps.getFromIngredientsList(i).orElseThrow(() -> new AssertionError(String.format("Ingredient %s does not exist", i))))
                .collect(toList());
        return existingIngredients.stream().map(i -> assumeIngredientRelatedToRecipe(i, recipe)).collect(toList());
    }

    @Step("Assume ingredient {0} is not in recipe {1} ingredients list")
    public void assumeIngredientNotRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        getFromRecipeIngredientsList(ingredient, recipe).ifPresent(
                i -> recipeIngredientRemovalServiceClient.removeIngredientFromRecipeAsSuperAdministrator(ingredient, recipe));
        assumeThat(getFromRecipeIngredientsList(ingredient, recipe)).isEmpty();
    }

    @Step("Assert recipe ingredients {0} are in recipe ingredients list {1}")
    public void assertInRecipeIngredientsList(Collection<RecipeIngredientValue> expected, Collection<RecipeIngredientValue> actual) {
        assertThat(actual).usingElementComparator(ID_AND_QUANTITY_COMPARATOR).containsAll(expected);
    }

    @Step("Assert ingredient {0} is in recipe {1} ingredients list")
    public void assertIngredientRelatedToRecipe(@NonNull RecipeIngredientValue recipeIngredient, @NonNull RecipeValue recipe) {
        assertThat(getFromRecipeIngredientsList(recipeIngredient, recipe)).isPresent().get()
                .usingComparator(ID_AND_QUANTITY_COMPARATOR).isEqualTo(recipeIngredient);
    }

    @Step("Assert ingredient {0} is not in recipe {1} ingredients list")
    public void assertIngredientNotRelatedToRecipe(@NonNull RecipeIngredientValue recipeIngredient, @NonNull RecipeValue recipe) {
        assertThat(getFromRecipeIngredientsList(recipeIngredient, recipe)).isNotPresent();
    }

    @Step("Get recipe {0} ingredients list")
    public Collection<RecipeIngredientValue> getRecipeIngredients(@NonNull RecipeValue recipe) {
        return fetchRecipeIngredients(recipe);
    }

}
