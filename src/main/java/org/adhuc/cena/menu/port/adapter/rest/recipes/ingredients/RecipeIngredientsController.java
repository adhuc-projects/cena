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

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.recipes.RecipeAppService;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeIngredientAppService;
import org.adhuc.cena.menu.recipes.RemoveIngredientsFromRecipe;

/**
 * A REST controller exposing /api/recipes/{recipeId}/ingredients resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
@RestController
@Validated
@ExposesResourceFor(RecipeIngredientModel.class)
@RequestMapping(path = "/api/recipes/{recipeId}/ingredients", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class RecipeIngredientsController {

    private final EntityLinks links;
    private final RecipeIngredientModelAssembler modelAssembler;
    private final RecipeAppService recipeAppService;
    private final RecipeIngredientAppService recipeIngredientAppService;

    /**
     * Gets the recipe ingredient information for all ingredients linked to the recipe.
     */
    @GetMapping
    @ResponseStatus(OK)
    CollectionModel<RecipeIngredientModel> getRecipeIngredients(@PathVariable String recipeId) {
        return modelAssembler.toCollectionModel(recipeId, recipeAppService.getRecipe(new RecipeId(recipeId)).ingredients());
    }

    /**
     * Creates a recipe ingredient.
     */
    @PostMapping
    ResponseEntity<Void> createRecipeIngredient(@PathVariable String recipeId,
                                                @RequestBody @Valid CreateRecipeIngredientRequest request,
                                                Errors errors) throws URISyntaxException {
        recipeIngredientAppService.addIngredientToRecipe(request.toCommand(new RecipeId(recipeId)));
        return ResponseEntity.created(new URI(links.linkFor(RecipeIngredientModel.class, recipeId)
                .slash(request.getId()).withSelfRel().getHref())).build();
    }

    /**
     * Deletes all recipe ingredients.
     */
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    void deleteRecipeIngredients(@PathVariable String recipeId) {
        recipeIngredientAppService.removeIngredientsFromRecipe(new RemoveIngredientsFromRecipe(new RecipeId(recipeId)));
    }

}
