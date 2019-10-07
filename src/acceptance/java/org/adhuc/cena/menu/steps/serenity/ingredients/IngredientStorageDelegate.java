package org.adhuc.cena.menu.steps.serenity.ingredients;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Supplier;

import net.serenitybdd.core.Serenity;

/**
 * An ingredient storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
final class IngredientStorageDelegate {

    static final String ASSUMED_INGREDIENTS_SESSION_KEY = "assumedIngredients";
    static final String INGREDIENTS_SESSION_KEY = "ingredients";
    static final String INGREDIENT_SESSION_KEY = "ingredient";

    private final IngredientListClientDelegate listClient;

    IngredientStorageDelegate(String ingredientsResourceUrl) {
        listClient = new IngredientListClientDelegate(ingredientsResourceUrl);
    }

    /**
     * Stores the ingredients, using the default {#value INGREDIENTS_SESSION_KEY} key.
     *
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    public List<IngredientValue> storeIngredients(List<IngredientValue> ingredients) {
        return storeIngredients(INGREDIENTS_SESSION_KEY, ingredients);
    }

    /**
     * Stores the ingredients.
     *
     * @param sessionKey  the session key.
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    public List<IngredientValue> storeIngredients(String sessionKey, List<IngredientValue> ingredients) {
        Serenity.setSessionVariable(sessionKey).to(ingredients);
        return ingredients;
    }

    /**
     * Gets the stored ingredients, or fails if ingredients were not previously stored. By default, the
     * {#value INGREDIENTS_SESSION_KEY} key is used.
     *
     * @return the stored ingredients.
     * @throws AssertionError if ingredients were not previously stored.
     */
    public List<IngredientValue> storedIngredients() {
        return storedIngredients(INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the stored ingredients, or fails if ingredients were not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored ingredients.
     * @throws AssertionError if ingredients were not previously stored.
     */
    public List<IngredientValue> storedIngredients(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Ingredients in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
    }

    /**
     * Gets the stored ingredients, or fetches the list and stores it. By default, the {#value INGREDIENTS_SESSION_KEY}
     * key is used.
     *
     * @return the stored ingredients.
     */
    public List<IngredientValue> storedOrRetrievedIngredients() {
        return storedOrRetrievedIngredients(INGREDIENTS_SESSION_KEY, () -> listClient.fetchIngredients());
    }

    /**
     * Gets the stored ingredients, or retrieves it through the specified supplier and stores it.
     *
     * @param sessionKey          the session key.
     * @param ingredientsSupplier the ingredients supplier.
     * @return the stored ingredients.
     */
    public List<IngredientValue> storedOrRetrievedIngredients(String sessionKey, Supplier<List<IngredientValue>> ingredientsSupplier) {
        if (Serenity.hasASessionVariableCalled(sessionKey)) {
            return Serenity.sessionVariableCalled(sessionKey);
        }
        return storeIngredients(sessionKey, ingredientsSupplier.get());
    }

    /**
     * Stores the ingredient, using the default {#value INGREDIENT_SESSION_KEY} key.
     *
     * @param ingredient the ingredient to store.
     * @return the ingredient.
     */
    public IngredientValue storeIngredient(IngredientValue ingredient) {
        return storeIngredient(INGREDIENT_SESSION_KEY, ingredient);
    }

    /**
     * Stores the ingredient.
     *
     * @param sessionKey the session key.
     * @param ingredient the ingredient to store.
     * @return the ingredient.
     */
    public IngredientValue storeIngredient(String sessionKey, IngredientValue ingredient) {
        Serenity.setSessionVariable(sessionKey).to(ingredient);
        return ingredient;
    }

    /**
     * Gets the stored ingredient, or fails if ingredient was not previously stored. By default, the
     * {#value INGREDIENT_SESSION_KEY} key is used.
     *
     * @return the stored ingredient.
     * @throws AssertionError if ingredient was not previously stored.
     */
    public IngredientValue storedIngredient() {
        return storedIngredient(INGREDIENT_SESSION_KEY);
    }

    /**
     * Gets the stored ingredient, or fails if ingredient was not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored ingredient.
     * @throws AssertionError if ingredient was not previously stored.
     */
    public IngredientValue storedIngredient(String sessionKey) {
        IngredientValue ingredient = Serenity.sessionVariableCalled(sessionKey);
        assertThat(ingredient).isNotNull();
        return ingredient;
    }

}
