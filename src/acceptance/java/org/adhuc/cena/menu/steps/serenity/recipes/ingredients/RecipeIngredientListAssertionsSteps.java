/*
 * Copyright (C) 2019-2020 Alexandre Carbenay
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

import static org.assertj.core.api.Assertions.assertThat;

import static org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientValue.ID_AND_QUANTITY_COMPARATOR;

import java.util.Collection;

import lombok.NonNull;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

/**
 * The recipe's ingredients list rest-service client steps definition dedicated to assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeIngredientListAssertionsSteps {

    private final RecipeIngredientListClientDelegate listClient = new RecipeIngredientListClientDelegate();

    @Step("Assert empty recipe {0} ingredients list")
    public void assertEmptyRecipeIngredientList(RecipeValue recipe) {
        assertThat(listClient.fetchRecipeIngredients(recipe)).isEmpty();
    }

    @Step("Assert empty recipe ingredients list {0}")
    public void assertEmptyRecipeIngredientList(Collection<RecipeIngredientValue> recipeIngredients) {
        assertThat(recipeIngredients).isEmpty();
    }

    @Step("Assert recipe ingredients {0} are in recipe ingredients list {1}")
    public void assertInRecipeIngredientsList(Collection<RecipeIngredientValue> expected, Collection<RecipeIngredientValue> actual) {
        assertThat(actual).usingElementComparator(ID_AND_QUANTITY_COMPARATOR).containsAll(expected);
    }

    @Step("Assert ingredient {0} is in recipe {1} ingredients list")
    public void assertIngredientRelatedToRecipe(@NonNull RecipeIngredientValue recipeIngredient, @NonNull RecipeValue recipe) {
        assertThat(listClient.getFromRecipeIngredientsList(recipeIngredient, recipe)).isPresent().get()
                .usingComparator(ID_AND_QUANTITY_COMPARATOR).isEqualTo(recipeIngredient);
    }

    @Step("Assert ingredient {0} is not in recipe {1} ingredients list")
    public void assertIngredientNotRelatedToRecipe(@NonNull RecipeIngredientValue recipeIngredient, @NonNull RecipeValue recipe) {
        assertThat(listClient.getFromRecipeIngredientsList(recipeIngredient, recipe)).isNotPresent();
    }

}
