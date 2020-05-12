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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.recipes.RecipeConsultationAppService;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeAuthoringAppService;
import org.adhuc.cena.menu.recipes.RemoveIngredientFromRecipe;

/**
 * A REST controller exposing /api/recipes/{recipeId}/ingredients/{ingredientId} resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/recipes/{recipeId}/ingredients/{ingredientId}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class RecipeIngredientController {

    private final RecipeIngredientModelAssembler modelAssembler;
    private final RecipeAuthoringAppService recipeIngredientAppService;
    private final RecipeConsultationAppService recipeAppService;

    /**
     * Gets the recipe ingredient information for the recipe ingredient corresponding to the specified recipe and
     * ingredient identities.
     *
     * @param recipeId the recipe identity.
     * @param ingredientId the ingredient identity.
     * @return the recipe ingredient information.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    RecipeIngredientModel getRecipeIngredient(@PathVariable String recipeId, @PathVariable String ingredientId) {

        var recipe = recipeAppService.getRecipe(new RecipeId(recipeId));
        var ingredient = recipe.ingredient(new IngredientId(ingredientId));
        return modelAssembler.toModel(ingredient);
    }

    /**
     * Deletes the recipe ingredient corresponding to the specified recipe and ingredient identities.
     *
     * @param recipeId the recipe identity.
     * @param ingredientId the ingredient identity.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable String recipeId, @PathVariable String ingredientId) {
        recipeIngredientAppService.removeIngredientFromRecipe(new RemoveIngredientFromRecipe(
                new IngredientId(ingredientId), new RecipeId(recipeId)));
    }

}
