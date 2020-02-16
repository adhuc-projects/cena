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

import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.ingredients.IngredientValue.NAME_AND_MEASUREMENT_TYPES_COMPARATOR;

import java.util.Collection;
import java.util.List;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeListSteps;
import org.adhuc.cena.menu.steps.serenity.recipes.ingredients.RecipeIngredientRemovalSteps;
import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The ingredients list rest-service client steps definition dedicated to assumptions.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class IngredientListAssumptionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(
            resourceUrlResolverDelegate.ingredientsResourceUrl());

    @Steps
    private IngredientCreationSteps ingredientCreation;
    @Steps
    private IngredientDeletionSteps ingredientDeletion;
    @Steps
    private RecipeListSteps recipeList;
    @Steps
    private RecipeIngredientRemovalSteps recipeIngredientRemoval;

    @Step("Assume empty ingredients list")
    public void assumeEmptyIngredientsList() {
        ingredientDeletion.deleteIngredientsAsSuperAdministrator();
        assumeThat(listClient.fetchIngredients()).isEmpty();
    }

    @Step("Assume ingredient {0} is in ingredients list")
    public IngredientValue assumeInIngredientsList(IngredientValue ingredient) {
        return assumeInIngredientsList(List.of(ingredient)).stream().findFirst().get();
    }

    @Step("Assume ingredients {0} are in ingredients list")
    public Collection<IngredientValue> assumeInIngredientsList(Collection<IngredientValue> ingredients) {
        var existingIngredients = listClient.fetchIngredients();
        dissociateRecipesFromIngredients(existingIngredients);
        deleteIngredientsWithDifferentDefinitionThanExpected(ingredients, existingIngredients);
        var allIngredients = listClient.fetchIngredients();
        assumeThat(allIngredients).usingElementComparator(NAME_AND_MEASUREMENT_TYPES_COMPARATOR).containsAll(ingredients);
        return allIngredients.stream().filter(i -> ingredients.stream()
                .anyMatch(i2 -> NAME_AND_MEASUREMENT_TYPES_COMPARATOR.compare(i, i2) == 0)).collect(toList());
    }

    private void dissociateRecipesFromIngredients(Collection<IngredientValue> ingredients) {
        ingredients.stream()
                .forEach(ingredient -> {
                    recipeList.getRecipesComposedOfIngredient(ingredient).stream().forEach(
                            recipe -> recipeIngredientRemoval.removeIngredientFromRecipeAsSuperAdministrator(ingredient, recipe)
                    );
                });
    }

    private void deleteIngredientsWithDifferentDefinitionThanExpected(Collection<IngredientValue> expectedIngredients,
                                                                      Collection<IngredientValue> existingIngredients) {
        expectedIngredients.stream()
                .filter(ingredient -> existingIngredients.stream()
                        .noneMatch(existing -> NAME_AND_MEASUREMENT_TYPES_COMPARATOR.compare(existing, ingredient) == 0))
                .forEach(ingredient -> {
                    var ingredientToDelete = existingIngredients.stream().filter(i -> COMPARATOR.compare(i, ingredient) == 0).findFirst();
                    ingredientToDelete.ifPresent(i -> ingredientDeletion.deleteIngredientAsIngredientManager(i));
                    ingredientCreation.createIngredientAsIngredientManager(ingredient);
                });
    }

    @Step("Assume ingredient {0} is not in ingredients list")
    public IngredientValue assumeNotInIngredientsList(IngredientValue ingredient) {
        listClient.getFromIngredientsList(ingredient).ifPresent(i -> {
            dissociateRecipesFromIngredients(List.of(i));
            ingredientDeletion.deleteIngredientAsIngredientManager(i);
        });
        assumeThat(listClient.fetchIngredients()).usingElementComparator(COMPARATOR).doesNotContain(ingredient);
        return ingredient;
    }

}
