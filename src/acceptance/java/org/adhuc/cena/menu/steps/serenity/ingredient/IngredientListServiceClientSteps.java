package org.adhuc.cena.menu.steps.serenity.ingredient;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.springframework.http.HttpStatus.OK;

import java.util.Collections;
import java.util.List;

import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.AbstractServiceClientSteps;

/**
 * The ingredients list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientListServiceClientSteps extends AbstractServiceClientSteps {

    @Step("Assume empty ingredients list")
    public void assumeEmptyIngredientsList() {
        // TODO remove existing ingredients
        assumeThat(getIngredients()).isEmpty();
    }

    private List<Object> getIngredients() {
        rest().get(getIngredientsResourceUrl()).then().statusCode(OK.value()).extract().jsonPath();
        // TODO get ingredients list from response
        return Collections.emptyList();
    }

    private String getIngredientsResourceUrl() {
        return getApiClientResource().getIngredients();
    }

}
