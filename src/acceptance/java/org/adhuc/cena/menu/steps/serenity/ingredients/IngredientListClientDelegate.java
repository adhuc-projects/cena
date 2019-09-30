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

import static net.serenitybdd.rest.SerenityRest.rest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.restassured.path.json.JsonPath;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.core.Serenity;

/**
 * A client used to retrieve and store ingredients list from the server.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@RequiredArgsConstructor
class IngredientListClientDelegate {

    static final String ASSUMED_INGREDIENTS_SESSION_KEY = "assumedIngredients";
    static final String INGREDIENTS_SESSION_KEY = "ingredients";

    private final String ingredientsResourceUrl;

    /**
     * Gets the ingredients from Serenity session, or fetches the list and stores it in Serenity session. By default,
     * the {#value INGREDIENTS_SESSION_KEY} key is used.
     *
     * @return the session stored ingredients.
     */
    List<IngredientValue> getSessionStoredIngredients() {
        return getSessionStoredIngredients(INGREDIENTS_SESSION_KEY, () -> fetchIngredients());
    }

    /**
     * Gets the ingredients from Serenity session, or retrieves it through the specified supplier and stores it in session.
     *
     * @param sessionKey the Serenity session key.
     * @param ingredientsSupplier the ingredients supplier.
     * @return the session stored ingredients.
     */
    List<IngredientValue> getSessionStoredIngredients(String sessionKey, Supplier<List<IngredientValue>> ingredientsSupplier) {
        if (Serenity.hasASessionVariableCalled(sessionKey)) {
            return Serenity.sessionVariableCalled(sessionKey);
        }
        return storeIngredients(sessionKey, ingredientsSupplier.get());
    }

    /**
     * Gets the ingredients from Serenity session, or fails if session does not contain those ingredients. By default,
     * the {#value INGREDIENTS_SESSION_KEY} key is used.
     *
     * @return the session stored ingredients.
     */
    List<IngredientValue> getStrictSessionStoredIngredients() {
        return getStrictSessionStoredIngredients(INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the ingredients from Serenity session, or fails if session does not contain those ingredients.
     *
     * @param sessionKey the Serenity session key.
     * @return the session stored ingredients.
     */
    List<IngredientValue> getStrictSessionStoredIngredients(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Ingredients in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
    }

    /**
     * Fetches the ingredients from server.
     *
     * @return the fetched ingredients.
     */
    List<IngredientValue> fetchIngredients() {
        return getRawIngredientList().getList("_embedded.data", IngredientValue.class);
    }

    /**
     * Stores the ingredients in session.
     *
     * @param sessionKey  the session key associated with stored ingredient.
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    List<IngredientValue> storeIngredients(String sessionKey, List<IngredientValue> ingredients) {
        Serenity.setSessionVariable(sessionKey).to(ingredients);
        return ingredients;
    }

    /**
     * Gets the ingredient corresponding to the specified one from the ingredients list if existing, based on its name.
     * The ingredients list is retrieved from the server. If ingredient is retrieved from server, it should be populated
     * with all attributes, especially its identity and links.
     *
     * @param ingredient the ingredient to retrieve in the list.
     * @return the ingredient retrieved from list.
     */
    Optional<IngredientValue> getFromIngredientsList(IngredientValue ingredient) {
        return Optional.ofNullable(getRawIngredientList().param("name", ingredient.name())
                .getObject("_embedded.data.find { ingredient->ingredient.name == name }", IngredientValue.class));
    }

    /**
     * Retrieves ingredients list from the server.
     *
     * @return the {@link JsonPath} corresponding to the ingredients list.
     */
    private JsonPath getRawIngredientList() {
        return rest().get(ingredientsResourceUrl).then().statusCode(OK.value()).extract().jsonPath();
    }

}
