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
package org.adhuc.cena.menu.steps.serenity.ingredient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;

/**
 * The ingredients list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientListServiceClientSteps extends AbstractIngredientServiceClientSteps {

    private static final String INGREDIENTS = "ingredients";

    @Step("Assume empty ingredients list")
    public void assumeEmptyIngredientsList() {
        deleteIngredients();
        assumeThat(fetchIngredients()).isEmpty();
    }

    @Step("Assert empty ingredients list")
    public void assertEmptyIngredientsList() {
        assertThat(getSessionStoredIngredients()).isEmpty();
    }

    @Step("Assume ingredient {0} is not in ingredients list")
    public void assumeNotInIngredientsList(IngredientValue ingredient) {
        storeIngredient(ingredient);
        // TODO remove ingredient if existing
        assumeThat(fetchIngredients()).doesNotContain(ingredient);
    }

    @Step("Assert ingredient is in ingredients list")
    public void assertIngredientInIngredientsList() {
        assertIngredientInIngredientsList(ingredient());
    }

    @Step("Assert ingredient {0} is in ingredients list")
    public void assertIngredientInIngredientsList(IngredientValue ingredient) {
        assertThat(getIngredientFromIngredientsList(ingredient)).isPresent();
    }

    @Step("Get ingredients list (session)")
    public List<IngredientValue> getIngredients() {
        return getSessionStoredIngredients();
    }

    /**
     * Fetches the ingredients from server.
     */
    private List<IngredientValue> fetchIngredients() {
        var jsonPath = rest().get(getIngredientsResourceUrl()).then().statusCode(OK.value()).extract().jsonPath();
        return jsonPath.getList("_embedded.data", IngredientValue.class);
    }

    /**
     * Gets the ingredients from Serenity session, or fetches the list and stores it in Serenity session.
     */
    private List<IngredientValue> getSessionStoredIngredients() {
        if (Serenity.hasASessionVariableCalled(INGREDIENTS)) {
            return Serenity.sessionVariableCalled(INGREDIENTS);
        }
        var ingredients = fetchIngredients();
        Serenity.setSessionVariable(INGREDIENTS).to(ingredients);
        return ingredients;
    }

    private Optional<IngredientValue> getIngredientFromIngredientsList(IngredientValue ingredient) {
        var jsonPath = rest().get(getIngredientsResourceUrl()).then().statusCode(OK.value()).extract().jsonPath();
        return Optional.ofNullable(jsonPath.param("name", ingredient.name())
                .getObject("_embedded.data.find { ingredient->ingredient.name == name }", IngredientValue.class));
    }

    private void deleteIngredients() {
        rest().delete(getIngredientsResourceUrl()).then().statusCode(NO_CONTENT.value());
    }

}
