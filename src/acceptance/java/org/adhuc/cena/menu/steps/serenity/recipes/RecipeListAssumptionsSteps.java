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

import static java.util.stream.Collectors.toList;

import static org.assertj.core.api.Assumptions.assumeThat;

import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue.NAME_AND_CONTENT_COMPARATOR;
import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.AUTHENTICATED_USER;
import static org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType.INGREDIENT_MANAGER;

import java.util.Collection;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.ingredients.IngredientListServiceClientSteps;
import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * The recipes list rest-service client steps definition dedicated to assumptions.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class RecipeListAssumptionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final RecipeListClientDelegate listClient = new RecipeListClientDelegate(
            resourceUrlResolverDelegate.recipesResourceUrl());

    @Steps
    private RecipeCreationServiceClientSteps recipeCreationServiceClient;
    @Steps
    private RecipeDeletionServiceClientSteps recipeDeletionServiceClient;

    @Step("Assume empty recipes list")
    public void assumeEmptyRecipesList() {
        recipeDeletionServiceClient.deleteRecipesAsSuperAdministrator();
        assumeThat(listClient.fetchRecipes()).isEmpty();
    }

    @Step("Assume recipe {0} is in recipes list")
    public RecipeValue assumeInRecipesList(RecipeValue recipe) {
        var author = AuthenticationProvider.instance().isAuthenticated()
                ? AuthenticationProvider.instance().currentAuthentication()
                : AUTHENTICATED_USER;
        return assumeInRecipesList(recipe, author);
    }

    private RecipeValue assumeInRecipesList(RecipeValue recipe, AuthenticationType author) {
        if (listClient.getFromRecipesList(recipe).isEmpty()) {
            recipeCreationServiceClient.createRecipeAs(recipe, author);
        }
        var recipes = listClient.fetchRecipes();
        assumeThat(recipes).usingElementComparator(COMPARATOR).contains(recipe);
        return recipes.stream().filter(i -> COMPARATOR.compare(i, recipe) == 0).findFirst().get();
    }

    @Step("Assume recipe {0} is in recipes list and authored by currently authenticated user")
    public RecipeValue assumeInRecipesListAuthoredByCurrentUser(RecipeValue recipe) {
        var author = AuthenticationProvider.instance().currentAuthentication();
        return assumeInRecipesListAuthoredBy(recipe, author);
    }

    @Step("Assume recipe {0} is in recipes list and authored by another user than currently authenticated")
    public RecipeValue assumeInRecipesListAuthoredByAnotherUser(RecipeValue recipe) {
        var author = AuthenticationProvider.instance().currentAuthentication() != AUTHENTICATED_USER
                ? AUTHENTICATED_USER : INGREDIENT_MANAGER;
        return assumeInRecipesListAuthoredBy(recipe, author);
    }

    @Step("Assume recipe {0} is in recipes list and has been authored by {1}")
    public RecipeValue assumeInRecipesListAuthoredBy(RecipeValue recipe, AuthenticationType authenticationType) {
        var existingRecipe = listClient.getFromRecipesList(recipe);
        if (existingRecipe.isPresent() && !existingRecipe.get().author().equals(authenticationType.toString())) {
            recipeDeletionServiceClient.deleteRecipeAsSuperAdministrator(existingRecipe.get());
        }
        return assumeInRecipesList(recipe, authenticationType);
    }

    @Step("Assume recipes {0} are in recipes list")
    public Collection<RecipeValue> assumeInRecipesList(Collection<RecipeValue> recipes) {
        var existingRecipes = listClient.fetchRecipes();
        recipes.stream()
                .filter(recipe -> existingRecipes.stream()
                        .noneMatch(existing -> NAME_AND_CONTENT_COMPARATOR.compare(existing, recipe) == 0))
                .forEach(recipe -> recipeCreationServiceClient.createRecipeAsAuthenticatedUser(recipe));
        var allRecipes = listClient.fetchRecipes();
        assumeThat(allRecipes).usingElementComparator(NAME_AND_CONTENT_COMPARATOR).containsAll(recipes);
        return allRecipes.stream().filter(r -> recipes.stream().anyMatch(r2 -> NAME_AND_CONTENT_COMPARATOR.compare(r, r2) == 0)).collect(toList());
    }

    @Step("Assume recipe {0} is not in recipes list")
    public RecipeValue assumeNotInRecipesList(RecipeValue recipe) {
        listClient.getFromRecipesList(recipe).ifPresent(r -> recipeDeletionServiceClient.deleteRecipeAsSuperAdministrator(r));
        assumeThat(listClient.fetchRecipes()).usingElementComparator(COMPARATOR).doesNotContain(recipe);
        return recipe;
    }

}
