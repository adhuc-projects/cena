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

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.security.AsAuthenticatedUser;
import org.adhuc.cena.menu.common.security.AsSuperAdministrator;

/**
 * A {@link RecipeAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RecipeAppServiceImpl implements RecipeAppService {

    @NonNull
    private RecipeRepository repository;

    @Override
    public List<Recipe> getRecipes() {
        return List.copyOf(repository.findAll());
    }

    @Override
    public Recipe getRecipe(@NonNull RecipeId recipeId) {
        return repository.findNotNullById(recipeId);
    }

    @Override
    @AsAuthenticatedUser
    public Recipe createRecipe(@NonNull CreateRecipe command) {
        log.info("Create recipe from command {}", command);
        var recipe = new Recipe(command.recipeId(), command.recipeName(), command.recipeContent(), command.recipeAuthor());
        return repository.save(recipe);
    }

    @Override
    @AsSuperAdministrator
    public void deleteRecipes() {
        repository.deleteAll();
    }

    @Override
    @AsSuperAdministrator
    public void deleteRecipe(@NonNull DeleteRecipe command) {
        repository.delete(repository.findNotNullById(command.recipeId()));
    }

}
