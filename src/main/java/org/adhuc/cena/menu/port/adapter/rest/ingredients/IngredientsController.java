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
package org.adhuc.cena.menu.port.adapter.rest.ingredients;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientAppService;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.port.adapter.rest.InvalidRestRequestException;
import org.adhuc.cena.menu.port.adapter.rest.support.ListResource;

/**
 * A REST controller exposing /api/ingredients resource.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@RestController
@ExposesResourceFor(Ingredient.class)
@RequestMapping(path = "/api/ingredients", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class IngredientsController {

    private EntityLinks links;
    private IngredientAppService ingredientAppService;
    private IngredientResourceAssembler resourceAssembler;

    IngredientsController(EntityLinks links, IngredientAppService ingredientAppService, IngredientResourceAssembler resourceAssembler) {
        this.links = links;
        this.ingredientAppService = ingredientAppService;
        this.resourceAssembler = resourceAssembler;
    }

    /**
     * Gets the ingredient information for all ingredients.
     */
    @GetMapping
    @ResponseStatus(OK)
    ListResource<IngredientResource> getIngredients() {
        var ingredients = ingredientAppService.getIngredients();
        return new ListResource<>(resourceAssembler.toResources(ingredients))
                .withLink(links.linkToCollectionResource(Ingredient.class).withSelfRel());
    }

    /**
     * Creates an ingredient.
     */
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    ResponseEntity<Void> createIngredient(@RequestBody @Valid CreateIngredientRequest request, Errors errors) throws URISyntaxException {
        var identity = IngredientId.generate();
        validateRequest(errors);
        ingredientAppService.createIngredient(request.toCommand(identity));

        return ResponseEntity.created(new URI(links.linkToSingleResource(Ingredient.class, identity).getHref())).build();
    }

    private void validateRequest(Errors errors) {
        if (errors.hasErrors()) {
            log.debug("Request validation raises errors : {}", errors);
            throw new InvalidRestRequestException();
        }
    }

}
