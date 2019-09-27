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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.IngredientNameComparator;

/**
 * The ingredients list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientListServiceClientSteps extends AbstractIngredientServiceClientSteps {

    private static final String ASSUMED_INGREDIENTS_SESSION_KEY = "assumedIngredients";
    private static final String INGREDIENTS_SESSION_KEY = "ingredients";

    @Steps
    private IngredientCreationServiceClientSteps ingredientCreationServiceClient;
    @Steps
    private IngredientDeletionServiceClientSteps ingredientDeletionServiceClient;
    private IngredientNameComparator ingredientNameComparator = new IngredientNameComparator();

    @Step("Assume empty ingredients list")
    public void assumeEmptyIngredientsList() {
        ingredientDeletionServiceClient.deleteIngredients();
        assumeThat(fetchIngredients()).isEmpty();
    }

    @Step("Assert empty ingredients list")
    public void assertEmptyIngredientsList() {
        assertThat(getSessionStoredIngredients()).isEmpty();
    }

    @Step("Assume ingredient {0} is in ingredients list")
    public void assumeInIngredientsList(IngredientValue ingredient) {
        storeIngredient(ingredient);
        // TODO add ingredient only if not already present
        ingredientCreationServiceClient.createIngredient(ingredient);
        assumeThat(fetchIngredients()).usingElementComparator(ingredientNameComparator).contains(ingredient);
    }

    @Step("Assume ingredients {0} are in ingredients list")
    public void assumeInIngredientsList(List<IngredientValue> ingredients) {
        storeIngredients(ASSUMED_INGREDIENTS_SESSION_KEY, ingredients);
        ingredients.forEach(ingredient -> ingredientCreationServiceClient.createIngredient(ingredient));
        assumeThat(fetchIngredients()).usingElementComparator(ingredientNameComparator).containsAll(ingredients);
    }

    @Step("Assume ingredient {0} is not in ingredients list")
    public void assumeNotInIngredientsList(IngredientValue ingredient) {
        storeIngredient(ingredient);
        getFromIngredientsList(ingredient).ifPresent(i -> ingredientDeletionServiceClient.deleteIngredient(i));
        assumeThat(fetchIngredients()).usingElementComparator(ingredientNameComparator).doesNotContain(ingredient);
    }

    @Step("Assert ingredient {0} is in ingredients list")
    public void assertInIngredientsList(IngredientValue ingredient) {
        assertThat(getFromIngredientsList(ingredient)).isPresent();
    }

    @Step("Assert ingredients {0} are in ingredients list")
    public void assertInIngredientsList(List<IngredientValue> ingredients) {
        assertThat(fetchIngredients()).usingElementComparator(ingredientNameComparator).containsAll(ingredients);
    }

    @Step("Assert ingredient {0} is not in ingredients list")
    public void assertNotInIngredientsList(IngredientValue ingredient) {
        assertThat(getFromIngredientsList(ingredient)).isNotPresent();
    }

    public List<IngredientValue> getAssumedIngredients() {
        assertThat(Serenity.hasASessionVariableCalled(ASSUMED_INGREDIENTS_SESSION_KEY))
                .as("Assumed ingredients must have been set previously").isTrue();
        return Serenity.sessionVariableCalled(ASSUMED_INGREDIENTS_SESSION_KEY);
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
        if (Serenity.hasASessionVariableCalled(INGREDIENTS_SESSION_KEY)) {
            return Serenity.sessionVariableCalled(INGREDIENTS_SESSION_KEY);
        }
        var ingredients = fetchIngredients();
        return storeIngredients(INGREDIENTS_SESSION_KEY, ingredients);
    }

    private List<IngredientValue> storeIngredients(String sessionKey, List<IngredientValue> ingredients) {
        Serenity.setSessionVariable(sessionKey).to(ingredients);
        return ingredients;
    }

    private Optional<IngredientValue> getFromIngredientsList(IngredientValue ingredient) {
        var jsonPath = rest().get(getIngredientsResourceUrl()).then().statusCode(OK.value()).extract().jsonPath();
        return Optional.ofNullable(jsonPath.param("name", ingredient.name())
                .getObject("_embedded.data.find { ingredient->ingredient.name == name }", IngredientValue.class));
    }

}
