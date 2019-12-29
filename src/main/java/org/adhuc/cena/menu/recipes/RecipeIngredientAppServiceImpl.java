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
package org.adhuc.cena.menu.recipes;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.security.AsAuthenticatedUser;

/**
 * A {@link RecipeIngredientAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RecipeIngredientAppServiceImpl implements RecipeIngredientAppService {

    private final IngredientToRecipeAdditionService ingredientToRecipeAdditionService;
    private final IngredientFromRecipeRemovalService ingredientFromRecipeRemovalService;
    private final RecipeRepository recipeRepository;

    @Override
    @AsRecipeAuthor
    public void addIngredientToRecipe(@NonNull AddIngredientToRecipe command) {
        ingredientToRecipeAdditionService.addIngredientToRecipe(command);
    }

    @Override
    @AsAuthenticatedUser
    public void removeIngredientFromRecipe(@NonNull RemoveIngredientFromRecipe command) {
        ingredientFromRecipeRemovalService.removeIngredientFromRecipe(command);
    }

    @Override
    @AsAuthenticatedUser
    public void removeIngredientsFromRecipe(@NonNull RemoveIngredientsFromRecipe command) {
        var recipe = recipeRepository.findNotNullById(command.recipeId());
        recipe.removeIngredients(command);
    }

}
