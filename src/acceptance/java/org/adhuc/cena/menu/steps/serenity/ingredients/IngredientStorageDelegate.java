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

import java.util.Collection;

import net.serenitybdd.core.Serenity;

/**
 * An ingredient storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public final class IngredientStorageDelegate {

    static final String ASSUMED_INGREDIENTS_SESSION_KEY = "assumedIngredients";
    static final String INGREDIENTS_SESSION_KEY = "ingredients";
    static final String INGREDIENT_SESSION_KEY = "ingredient";

    /**
     * Stores the ingredients, using the default {#value INGREDIENTS_SESSION_KEY} key.
     *
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    public Collection<IngredientValue> storeIngredients(Collection<IngredientValue> ingredients) {
        return storeIngredients(INGREDIENTS_SESSION_KEY, ingredients);
    }

    /**
     * Stores the assumed ingredients.
     *
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    public Collection<IngredientValue> storeAssumedIngredients(Collection<IngredientValue> ingredients) {
        return storeIngredients(ASSUMED_INGREDIENTS_SESSION_KEY, ingredients);
    }

    /**
     * Stores the ingredients.
     *
     * @param sessionKey  the session key.
     * @param ingredients the ingredients to store.
     * @return the ingredients.
     */
    Collection<IngredientValue> storeIngredients(String sessionKey, Collection<IngredientValue> ingredients) {
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
    public Collection<IngredientValue> storedIngredients() {
        return storedIngredients(INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the stored assumed ingredients, or fails if ingredients were not previously stored.
     *
     * @return the stored assumed ingredients.
     * @throws AssertionError if ingredients were not previously stored.
     */
    public Collection<IngredientValue> storedAssumedIngredients() {
        return storedIngredients(ASSUMED_INGREDIENTS_SESSION_KEY);
    }

    /**
     * Gets the stored ingredients, or fails if ingredients were not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored ingredients.
     * @throws AssertionError if ingredients were not previously stored.
     */
    Collection<IngredientValue> storedIngredients(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Ingredients in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
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
    IngredientValue storeIngredient(String sessionKey, IngredientValue ingredient) {
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
    IngredientValue storedIngredient(String sessionKey) {
        IngredientValue ingredient = Serenity.sessionVariableCalled(sessionKey);
        assertThat(ingredient).isNotNull();
        return ingredient;
    }

}
