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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.port.adapter.rest.support.Uuid;
import org.adhuc.cena.menu.recipes.QueryRecipes;
import org.adhuc.cena.menu.recipes.RecipeAuthoringAppService;
import org.adhuc.cena.menu.recipes.RecipeConsultationAppService;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A REST controller exposing /api/recipes resource.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Validated
@RestController
@ExposesResourceFor(RecipeModel.class)
@RequestMapping(path = "/api/recipes", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class RecipesController {

    private final EntityLinks links;
    private final RecipeConsultationAppService recipeConsultation;
    private final RecipeAuthoringAppService recipeAuthoring;
    private final RecipeModelAssembler modelAssembler;

    /**
     * Gets the recipe information for all recipes.
     */
    @GetMapping
    @ResponseStatus(OK)
    CollectionModel<RecipeModel> getRecipes(@RequestParam(name = "filter[ingredient]", required = false) @Uuid(propertyName = "filter[ingredient]") String ingredient) {
        var query = QueryRecipes.query();
        if (ingredient != null) {
            query = query.withIngredientId(new IngredientId(ingredient));
        }
        var recipes = recipeConsultation.getRecipes(query);
        return modelAssembler.toCollectionModel(recipes);
    }

    /**
     * Creates a recipe.
     */
    @PostMapping
    ResponseEntity<Void> createRecipe(@RequestBody @Valid CreateRecipeRequest request, Errors errors, Principal principal) throws URISyntaxException {
        var identity = RecipeId.generate();
        recipeAuthoring.createRecipe(request.toCommand(identity, principal.getName()));
        return ResponseEntity.created(new URI(links.linkToItemResource(RecipeModel.class, identity).getHref())).build();
    }

}
