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

import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.COMPARATOR;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import io.restassured.path.json.JsonPath;
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

    @Step("Assume recipe {0} contains no ingredient")
    public void assumeRecipeContainsNoIngredient(@NonNull RecipeValue recipe) {
        // TODO delete recipe ingredients if not empty
        assumeThat(fetchRecipeIngredients(recipe)).isEmpty();
    }

    @Step("Assert empty recipe ingredients list {0}")
    public void assertEmptyRecipeIngredientList(Collection<RecipeIngredientValue> recipeIngredients) {
        assertThat(recipeIngredients).isEmpty();
    }

    @Step("Assume ingredient {0} is in recipe {1} ingredients list")
    public RecipeIngredientValue assumeIngredientRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        if (getFromRecipeIngredientsList(ingredient, recipe).isEmpty()) {
            recipeIngredientAdditionServiceClient.addIngredientToRecipe(ingredient, recipe);
        }
        var recipeIngredient = getFromRecipeIngredientsList(ingredient, recipe);
        assumeThat(recipeIngredient).isPresent();
        return recipeIngredient.get();
    }

    @Step("Assume ingredients {0} are in recipe {1} ingredients list")
    public Collection<RecipeIngredientValue> assumeIngredientsRelatedToRecipe(List<IngredientValue> ingredients, RecipeValue recipe) {
        var existingIngredients = ingredients.stream()
                .map(i -> ingredientListServiceClientSteps.getFromIngredientsList(i))
                .map(i -> i.orElseThrow(() -> new AssertionError(String.format("Ingredient %s does not exist", i))))
                .collect(toList());
        return existingIngredients.stream().map(i -> assumeIngredientRelatedToRecipe(i, recipe)).collect(toList());
    }

    @Step("Assume ingredient {0} is not in recipe {1} ingredients list")
    public void assumeIngredientNotRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        // TODO delete relation if existing
        assumeThat(getFromRecipeIngredientsList(ingredient, recipe)).isEmpty();
    }

    @Step("Assert recipe ingredients {0} are in recipe ingredients list {1}")
    public void assertInRecipeIngredientsList(Collection<RecipeIngredientValue> expected, Collection<RecipeIngredientValue> actual) {
        assertThat(actual).usingElementComparator(COMPARATOR).containsAll(expected);
    }

    @Step("Assert ingredient {0} is in recipe {1} ingredients list")
    public void assertIngredientRelatedToRecipe(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
        assertThat(getFromRecipeIngredientsList(ingredient, recipe)).isPresent().get()
                .usingComparator(COMPARATOR).isEqualTo(new RecipeIngredientValue(ingredient));
    }

    @Step("Get recipe {0} ingredients list")
    public Collection<RecipeIngredientValue> getRecipeIngredients(@NonNull RecipeValue recipe) {
        return fetchRecipeIngredients(recipe);
    }

    /**
     * Fetches the recipe's ingredients from server.
     *
     * @param  recipe the recipe.
     * @return the fetched recipe ingredients.
     */
    private List<RecipeIngredientValue> fetchRecipeIngredients(@NonNull RecipeValue recipe) {
        return getRawRecipeIngredientList(recipe).getList("_embedded.data", RecipeIngredientValue.class);
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
        return Optional.ofNullable(getRawRecipeIngredientList(recipe).param("id", ingredient.id())
                .getObject("_embedded.data.find { ingredient->ingredient.id == id }", RecipeIngredientValue.class));
    }

    /**
     * Retrieves recipe's ingredients list from the server.
     *
     * @param recipe the recipe to retrieve ingredients for.
     * @return the {@link JsonPath} corresponding to the recipe ingredients list.
     */
    private JsonPath getRawRecipeIngredientList(@NonNull RecipeValue recipe) {
        var response = rest().get(recipe.getIngredients()).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath();
    }

}
