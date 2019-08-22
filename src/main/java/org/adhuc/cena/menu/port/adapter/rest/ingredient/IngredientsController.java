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
package org.adhuc.cena.menu.port.adapter.rest.ingredient;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.port.adapter.rest.support.HalResource;

/**
 * A REST controller exposing /api/ingredients resource.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@RestController
@ExposesResourceFor(Ingredient.class)
@RequestMapping(path = "/api/ingredients", produces = { HAL_JSON_VALUE, APPLICATION_JSON_VALUE })
public class IngredientsController {

    public static final String EMBEDDED_DATA_KEY = "data";

    private EntityLinks links;
    private IngredientAppService ingredientAppService;

    IngredientsController(EntityLinks links, IngredientAppService ingredientAppService) {
        this.links = links;
        this.ingredientAppService = ingredientAppService;
    }

    /**
     * Gets the ingredient information for all ingredients.
     */
    @GetMapping
    @ResponseStatus(OK)
    HalResource getIngredients() {
        // TODO implementation
        return new HalResource().withLink(links.linkToCollectionResource(Ingredient.class))
                .embedResource(EMBEDDED_DATA_KEY, ingredientAppService.getIngredients());
    }

    /**
     * Creates an ingredient.
     */
    @PostMapping(consumes = { APPLICATION_JSON_VALUE, HAL_JSON_VALUE })
    @ResponseStatus(CREATED)
    HttpHeaders createIngredient(@RequestBody CreateIngredientRequest request) throws URISyntaxException {
        // TODO validate incoming request
        ingredientAppService.createIngredient(request.toCommand());

        // TODO implementation
        var httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("http://cena.adhuc.org/ingredients/1"));
        return httpHeaders;
    }

    /**
     * Deletes all ingredients.
     */
    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    void deleteIngredients() {
        // TODO set security constraint: only super administrator can delete all ingredients
        ingredientAppService.deleteIngredients();
    }

}
