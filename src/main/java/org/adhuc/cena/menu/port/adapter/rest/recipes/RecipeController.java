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

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.recipes.DeleteRecipe;
import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A REST controller exposing /api/recipes/{recipeId} resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RestController
@RequestMapping(path = "/api/recipes/{recipeId}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class RecipeController {

    private EntityLinks links;
    private RecipeAppService recipeAppService;

    RecipeController(EntityLinks links, RecipeAppService recipeAppService) {
        this.links = links;
        this.recipeAppService = recipeAppService;
    }

    /**
     * Gets the recipe information for the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe information.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RecipeModel getRecipe(@PathVariable String recipeId) {
        var recipe = recipeAppService.getRecipe(new RecipeId(recipeId));
        var model = new RecipeModel(recipe);
        model.add(links.linkToItemResource(Recipe.class, recipeId).withSelfRel());
        return model;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable String recipeId) {
        recipeAppService.deleteRecipe(new DeleteRecipe(new RecipeId(recipeId)));
    }

}
