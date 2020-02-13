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

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * A client used to retrieve recipes from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
final class RecipeListClientDelegate {

    private static final String INGREDIENT_PARAM = "filter[ingredient]";

    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @NonNull
    private final String recipesResourceUrl;

    /**
     * Fetches the recipes from server.
     *
     * @return the fetched recipes.
     */
    public List<RecipeValue> fetchRecipes() {
        return fetchRecipesFromRawRecipeList(() -> getRawRecipeList(restClientDelegate::rest));
    }

    /**
     * Fetches the recipes composed of ingredient from server.
     *
     * @param ingredient the ingredient.
     * @return the fetched recipes.
     */
    public List<RecipeValue> fetchRecipesComposedOfIngredient(@NonNull IngredientValue ingredient) {
        return fetchRecipesFromRawRecipeList(() -> getRawRecipeList(getRecipeListComposedOfIngredientRequest(ingredient)));
    }

    /**
     * Attempts fetching the recipes composed of unknown ingredient from server, resulting in a not found response.
     *
     * @param ingredient the unknown ingredient.
     */
    public void attemptFetchingRecipesComposedOfUnknownIngredient(@NonNull IngredientValue ingredient) {
        getRecipeListComposedOfIngredientRequest(ingredient).get().get(recipesResourceUrl).then();
    }

    /**
     * Fetches the recipes from the specified recipes list retrieved from the server. This method factorizes the list
     * extraction from raw JSON path.
     *
     * @param rawRecipeListSupplier the recipes list supplier.
     * @return the fetched recipes.
     */
    private List<RecipeValue> fetchRecipesFromRawRecipeList(Supplier<JsonPath> rawRecipeListSupplier) {
        return rawRecipeListSupplier.get().getList("_embedded.data", RecipeValue.class);
    }

    /**
     * Gets the recipe corresponding to the specified one from the recipes list if existing, based on its name.
     * The recipes list is retrieved from the server. If recipe is retrieved from server, it should be populated
     * with all attributes, especially its identity and links.
     *
     * @param recipe the recipe to retrieve in the list.
     * @return the recipe retrieved from list.
     */
    public Optional<RecipeValue> getFromRecipesList(RecipeValue recipe) {
        return Optional.ofNullable(getRawRecipeList(restClientDelegate::rest).param("name", recipe.name())
                .getObject("_embedded.data.find { recipe->recipe.name == name }", RecipeValue.class));
    }

    /**
     * Gets a request supplier for recipes composed of ingredient.
     *
     * @param ingredient the ingredient.
     * @return the request supplier to use to retrieve recipes composed of ingredient.
     */
    private Supplier<RequestSpecification> getRecipeListComposedOfIngredientRequest(@NonNull IngredientValue ingredient) {
        return () -> restClientDelegate.rest().queryParam(INGREDIENT_PARAM, ingredient.id());
    }

    /**
     * Retrieves recipes list based on the supplied request specification. This method factorizes the server call,
     * response status assertion and JSON path extraction.
     *
     * @param requestSpecificationSupplier the request specification.
     * @return the {@link JsonPath} corresponding to the recipes list.
     */
    private JsonPath getRawRecipeList(Supplier<RequestSpecification> requestSpecificationSupplier) {
        var response = requestSpecificationSupplier.get().get(recipesResourceUrl).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath();
    }

}
