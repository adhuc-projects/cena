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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import java.util.List;
import java.util.Optional;

import io.restassured.path.json.JsonPath;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * A client used to retrieve ingredients from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@RequiredArgsConstructor
final class IngredientListClientDelegate {

    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @NonNull
    private final String ingredientsResourceUrl;

    /**
     * Fetches the ingredients from server.
     *
     * @return the fetched ingredients.
     */
    public List<IngredientValue> fetchIngredients() {
        return getRawIngredientList().getList("_embedded.data", IngredientValue.class);
    }

    /**
     * Gets the ingredient corresponding to the specified one from the ingredients list if existing, based on its name.
     * The ingredients list is retrieved from the server. If ingredient is retrieved from server, it should be populated
     * with all attributes, especially its identity and links.
     *
     * @param ingredient the ingredient to retrieve in the list.
     * @return the ingredient retrieved from list.
     */
    public Optional<IngredientValue> getFromIngredientsList(IngredientValue ingredient) {
        return Optional.ofNullable(getRawIngredientList().param("name", ingredient.name())
                .getObject("_embedded.data.find { ingredient->ingredient.name == name }", IngredientValue.class));
    }

    /**
     * Retrieves ingredients list from the server.
     *
     * @return the {@link JsonPath} corresponding to the ingredients list.
     */
    private JsonPath getRawIngredientList() {
        var response = restClientDelegate.rest().get(ingredientsResourceUrl).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath();
    }

}
