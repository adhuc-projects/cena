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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static java.util.Optional.ofNullable;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeIngredient;

/**
 * A REST controller exposing /api/recipes resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
@RestController
@ExposesResourceFor(RecipeIngredientModel.class)
@RequestMapping(path = "/api/recipes/{recipeId}/ingredients", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class RecipeIngredientsController {

    private final EntityLinks links;
    private final RecipeIngredientModelAssembler modelAssembler;

    private final Map<String, List<RecipeIngredient>> recipeIngredients = new HashMap<>();

    /**
     * Gets the recipe ingredient information for all ingredients linked to the recipe.
     */
    @GetMapping
    @ResponseStatus(OK)
    CollectionModel<RecipeIngredientModel> getRecipeIngredients(@PathVariable String recipeId) {
        return modelAssembler.toCollectionModel(recipeId, ofNullable(recipeIngredients.get(recipeId)).orElse(List.of()));
    }

    /**
     * Creates a recipe ingredient.
     */
    @PostMapping
    ResponseEntity<Void> createRecipeIngredient(@PathVariable String recipeId,
                                                @RequestBody CreateRecipeIngredientRequest request) throws URISyntaxException {
        addIngredientToRecipeIngredient(recipeId, request.getIngredientId());
        return ResponseEntity.created(new URI(links.linkFor(RecipeIngredientModel.class, recipeId)
                .slash(request.getIngredientId()).withSelfRel().getHref())).build();
    }

    private void addIngredientToRecipeIngredient(String recipeId, String ingredientId) {
        recipeIngredients.putIfAbsent(recipeId, new ArrayList<>());
        recipeIngredients.get(recipeId).add(new RecipeIngredient(new RecipeId(recipeId), new IngredientId(ingredientId)));
    }

    /**
     * For testing purposes only.
     */
    void clearRecipeIngredients() {
        recipeIngredients.clear();
    }

}
