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

import java.util.List;
import java.util.Optional;

import io.restassured.path.json.JsonPath;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue;
import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * A client used to retrieve recipe ingredients from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
final class RecipeIngredientListClientDelegate {

    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    /**
     * Fetches the recipe's ingredients from server.
     *
     * @param  recipe the recipe.
     * @return the fetched recipe ingredients.
     */
    public List<RecipeIngredientValue> fetchRecipeIngredients(@NonNull RecipeValue recipe) {
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
    public Optional<RecipeIngredientValue> getFromRecipeIngredientsList(@NonNull IngredientValue ingredient, @NonNull RecipeValue recipe) {
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
        var response = restClientDelegate.rest().get(recipe.getIngredients()).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath();
    }

}
