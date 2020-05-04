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

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientRepository;

/**
 * A domain service dedicated to ingredient to recipe addition. This service ensures that both ingredient and recipe
 * exist before proceeding.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IngredientToRecipeAdditionService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    /**
     * Adds an ingredient to a recipe, ensuring both ingredient and recipe exist before proceeding.
     *
     * @param command the ingredient to recipe addition command.
     * @throws EntityNotFoundException if either ingredient or recipe does not exist.
     */
    void addIngredientToRecipe(AddIngredientToRecipe command) {
        var ingredient = ingredientRepository.findById(command.ingredientId());
        if (ingredient.isEmpty()) {
            throw new EntityNotFoundException(Ingredient.class, command.ingredientId());
        }
        if (!command.quantity().unit().isAssociatedToOneOf(ingredient.get().measurementTypes())) {
            throw new InvalidMeasurementUnitForIngredientException(command.ingredientId(), command.recipeId(),
                    command.quantity().unit(), ingredient.get().measurementTypes());
        }
        var recipe = recipeRepository.findNotNullById(command.recipeId());
        recipe.addIngredient(command);
        recipeRepository.save(recipe);
    }

}
