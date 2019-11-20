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

import static net.serenitybdd.rest.SerenityRest.rest;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @NonNull
    private final String recipesResourceUrl;

    /**
     * Fetches the recipes from server.
     *
     * @return the fetched recipes.
     */
    public List<RecipeValue> fetchRecipes() {
        var response = rest().get(recipesResourceUrl).then();
        return statusAssertionDelegate.assertOk(response).extract().jsonPath().getList("_embedded.data", RecipeValue.class);
    }

}
