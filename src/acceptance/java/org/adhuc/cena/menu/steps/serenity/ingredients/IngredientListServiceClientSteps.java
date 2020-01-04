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

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.NAME_AND_MEASUREMENT_TYPES_COMPARATOR;

import java.util.Collection;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The ingredients list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
public class IngredientListServiceClientSteps {

    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(ingredientsResourceUrl());
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();

    @Steps
    private IngredientCreationServiceClientSteps ingredientCreationServiceClient;
    @Steps
    private IngredientDeletionServiceClientSteps ingredientDeletionServiceClient;

    @Step("Assume empty ingredients list")
    public void assumeEmptyIngredientsList() {
        ingredientDeletionServiceClient.deleteIngredientsAsSuperAdministrator();
        assumeThat(fetchIngredients()).isEmpty();
    }

    @Step("Assert empty ingredients list")
    public void assertEmptyIngredientsList() {
        assertEmptyIngredientsList(fetchIngredients());
    }

    @Step("Assert empty ingredients list {0}")
    public void assertEmptyIngredientsList(Collection<IngredientValue> ingredients) {
        assertThat(ingredients).isEmpty();
    }

    @Step("Assume ingredient {0} is in ingredients list")
    public IngredientValue assumeInIngredientsList(IngredientValue ingredient) {
        if (getFromIngredientsList(ingredient).isEmpty()) {
            ingredientCreationServiceClient.createIngredientAsIngredientManager(ingredient);
        }
        var ingredients = fetchIngredients();
        assumeThat(ingredients).usingElementComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).contains(ingredient);
        return ingredients.stream().filter(i -> NAME_AND_MEASUREMENT_TYPES_COMPARATOR.compare(i, ingredient) == 0).findFirst().get();
    }

    @Step("Assume ingredients {0} are in ingredients list")
    public Collection<IngredientValue> assumeInIngredientsList(Collection<IngredientValue> ingredients) {
        var existingIngredients = fetchIngredients();
        ingredients.stream()
                .filter(ingredient -> existingIngredients.stream()
                        .noneMatch(existing -> NAME_AND_MEASUREMENT_TYPES_COMPARATOR.compare(existing, ingredient) == 0))
                .forEach(ingredient -> ingredientCreationServiceClient.createIngredientAsIngredientManager(ingredient));
        var allIngredients = fetchIngredients();
        assumeThat(allIngredients).usingElementComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).containsAll(ingredients);
        return allIngredients.stream().filter(i -> ingredients.stream()
                .anyMatch(i2 -> NAME_AND_MEASUREMENT_TYPES_COMPARATOR.compare(i, i2) == 0)).collect(toList());
    }

    @Step("Assume ingredient {0} is not in ingredients list")
    public IngredientValue assumeNotInIngredientsList(IngredientValue ingredient) {
        getFromIngredientsList(ingredient).ifPresent(i -> ingredientDeletionServiceClient.deleteIngredientAsIngredientManager(i));
        assumeThat(fetchIngredients()).usingElementComparator(COMPARATOR).doesNotContain(ingredient);
        return ingredient;
    }

    @Step("Assert ingredient {0} is in ingredients list")
    public void assertInIngredientsList(IngredientValue ingredient) {
        assertThat(getFromIngredientsList(ingredient)).isPresent().get()
                .usingComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).isEqualTo(ingredient);
    }

    @Step("Assert ingredients {0} are in ingredients list {1}")
    public void assertInIngredientsList(Collection<IngredientValue> expected, Collection<IngredientValue> actual) {
        assertThat(actual).usingElementComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).containsAll(expected);
    }

    @Step("Assert ingredient {0} is not in ingredients list")
    public void assertNotInIngredientsList(IngredientValue ingredient) {
        assertThat(getFromIngredientsList(ingredient)).isNotPresent();
    }

    @Step("Get ingredients list")
    public Collection<IngredientValue> getIngredients() {
        return fetchIngredients();
    }

}
