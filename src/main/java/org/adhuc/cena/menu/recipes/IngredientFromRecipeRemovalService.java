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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.IngredientConsultationAppService;

/**
 * A domain service dedicated to ingredient from recipe removal. This service ensures that both ingredient and recipe
 * exist before proceeding.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IngredientFromRecipeRemovalService {

    private final RecipeRepository recipeRepository;
    private final IngredientConsultationAppService ingredientAppService;

    /**
     * Removes an ingredient from a recipe, ensuring both ingredient and recipe exist before proceeding.
     *
     * @param command the ingredient to recipe removal command.
     * @throws EntityNotFoundException if either ingredient or recipe does not exist.
     */
    void removeIngredientFromRecipe(RemoveIngredientFromRecipe command) {
        // Ensure that ingredient exists
        ingredientAppService.getIngredient(command.ingredientId());
        var recipe = recipeRepository.findNotNullById(command.recipeId());
        recipe.removeIngredient(command);
        recipeRepository.save(recipe);
    }

}
