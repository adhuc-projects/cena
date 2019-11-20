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
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A REST controller exposing /api/recipes resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Slf4j
@RestController
@ExposesResourceFor(Recipe.class)
@RequestMapping(path = "/api/recipes", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class RecipesController {

    private Map<RecipeId, Recipe> recipes = new HashMap<>();
    private EntityLinks links;

    RecipesController(EntityLinks links) {
        this.links = links;
    }

    /**
     * Gets the recipe information for all recipes.
     */
    @GetMapping
    @ResponseStatus(OK)
    CollectionModel<Object> getRecipes() {
        return new CollectionModel<>(Collections.emptyList())
                .add(links.linkToCollectionResource(Recipe.class).withSelfRel());
    }

    /**
     * Creates a recipe.
     */
    @PostMapping
    ResponseEntity<Void> createRecipe(@RequestBody CreateRecipeRequest request) throws URISyntaxException {
        var identity = RecipeId.generate();
        var recipe = new Recipe(identity, request.getName(), request.getContent());
        recipes.put(identity, recipe);

        return ResponseEntity.created(new URI(links.linkToItemResource(Recipe.class, identity).getHref())).build();
    }

    /**
     * Gets the recipe information for the recipe corresponding to the specified identity.
     *
     * @param recipeId the recipe identity.
     * @return the recipe information.
     */
    @GetMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    public RecipeModel getRecipe(@PathVariable int recipeId) {
        var identity = new RecipeId(recipeId);
        var recipe = recipes.get(identity);
        if (recipe == null) {
            throw new EntityNotFoundException(Recipe.class, identity);
        }
        var model = new RecipeModel(recipe);
        model.add(links.linkToItemResource(Recipe.class, recipeId).withSelfRel());
        return model;
    }

}
