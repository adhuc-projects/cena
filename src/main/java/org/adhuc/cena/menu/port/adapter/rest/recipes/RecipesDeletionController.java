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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.adhuc.cena.menu.recipes.RecipeAppService;

/**
 * A REST controller exposing /api/recipes resource for deletion.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RestController
@ConditionalOnProperty(name = "cena.menu-generation.features.recipes-deletion", matchIfMissing = true)
class RecipesDeletionController {

    private RecipeAppService recipeAppService;

    RecipesDeletionController(RecipeAppService recipeAppService) {
        this.recipeAppService = recipeAppService;
    }

    /**
     * Deletes all recipes.
     */
    @DeleteMapping(path = "/api/recipes")
    @ResponseStatus(NO_CONTENT)
    void deleteRecipes() {
        recipeAppService.deleteRecipes();
    }

}
