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
package org.adhuc.cena.menu.recipes;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.adhuc.cena.menu.common.ApplicationService;
import org.adhuc.cena.menu.common.security.AsAuthenticatedUser;

/**
 * A {@link RecipeAuthoring} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Slf4j
@AsRecipeAuthor
@RequiredArgsConstructor
@ApplicationService("recipeAuthoring")
class RecipeAuthoringImpl implements RecipeAuthoring {

    @NonNull
    private final RecipeCreation recipeCreation;
    @NonNull
    private final IngredientToRecipeAddition ingredientToRecipeAddition;
    @NonNull
    private final IngredientFromRecipeRemoval ingredientFromRecipeRemoval;
    @NonNull
    private final RecipeRepository recipeRepository;

    @Override
    @AsAuthenticatedUser
    public void createRecipe(@NonNull CreateRecipe command) {
        log.info("Create recipe from command {}", command);
        recipeCreation.createRecipe(command);
    }

    @Override
    public void deleteRecipe(@NonNull DeleteRecipe command) {
        recipeRepository.delete(recipeRepository.findNotNullById(command.recipeId()));
    }

    @Override
    public void addIngredientToRecipe(@NonNull AddIngredientToRecipe command) {
        ingredientToRecipeAddition.addIngredientToRecipe(command);
    }

    @Override
    public void removeIngredientFromRecipe(@NonNull RemoveIngredientFromRecipe command) {
        ingredientFromRecipeRemoval.removeIngredientFromRecipe(command);
    }

    @Override
    public void removeIngredientsFromRecipe(@NonNull RemoveIngredientsFromRecipe command) {
        var recipe = recipeRepository.findNotNullById(command.recipeId());
        recipe.removeIngredients(command);
    }

}
