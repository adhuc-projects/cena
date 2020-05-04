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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.DeleteIngredient;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * A REST controller exposing /api/ingredients/{ingredientId} resource.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ingredients/{ingredientId}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
class IngredientController {

    private final IngredientModelAssembler modelAssembler;
    private final IngredientAppService ingredientAppService;

    /**
     * Gets the ingredient information for the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient information.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public IngredientModel getIngredient(@PathVariable String ingredientId) {
        var ingredient = ingredientAppService.getIngredient(new IngredientId(ingredientId));
        return modelAssembler.toModel(ingredient);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable String ingredientId) {
        ingredientAppService.deleteIngredient(new DeleteIngredient(new IngredientId(ingredientId)));
    }

}
