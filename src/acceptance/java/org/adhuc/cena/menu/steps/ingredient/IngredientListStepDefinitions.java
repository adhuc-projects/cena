package org.adhuc.cena.menu.steps.ingredient;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredient.IngredientListServiceClientSteps;

/**
 * The ingredients list steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@StepDefAnnotation
public class IngredientListStepDefinitions {

    @Steps
    private IngredientListServiceClientSteps ingredientListServiceClient;

    @Given("^no existing ingredient$")
    public void noExistingIngredient() {
        ingredientListServiceClient.assumeEmptyIngredientsList();
    }

}
