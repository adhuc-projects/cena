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
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientRelatedService;

/**
 * An {@link IngredientRelatedService} implementation for recipes.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Service
@RequiredArgsConstructor
class RecipeIngredientRelatedService implements IngredientRelatedService {

    private final RecipeRepository recipeRepository;

    @Override
    public String relatedObjectName() {
        return "recipe";
    }

    @Override
    public boolean areIngredientsRelated() {
        return recipeRepository.findAll().stream().anyMatch(r -> !r.ingredients().isEmpty());
    }

    @Override
    public boolean isIngredientRelated(IngredientId ingredientId) {
        return recipeRepository.findAll().stream().anyMatch(
                r -> r.ingredients().stream().anyMatch(i -> i.ingredientId().equals(ingredientId)));
    }

}
