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

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.ingredients.IngredientConsultationAppService;

/**
 * A {@link RecipeConsultationAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RecipeConsultationAppServiceImpl implements RecipeConsultationAppService {

    @NonNull
    private final RecipeRepository recipeRepository;
    @NonNull
    private final IngredientConsultationAppService ingredientAppService;

    @Override
    public List<Recipe> getRecipes(@NonNull QueryRecipes query) {
        if (query.ingredientId().isPresent()) {
            var ingredient = ingredientAppService.getIngredient(query.ingredientId().get());
            return List.copyOf(recipeRepository.findByIngredient(ingredient.id()));
        }
        return List.copyOf(recipeRepository.findAll());
    }

    @Override
    public boolean exists(@NonNull RecipeId recipeId) {
        return recipeRepository.exists(recipeId);
    }

    @Override
    public Recipe getRecipe(@NonNull RecipeId recipeId) {
        return recipeRepository.findNotNullById(recipeId);
    }

}