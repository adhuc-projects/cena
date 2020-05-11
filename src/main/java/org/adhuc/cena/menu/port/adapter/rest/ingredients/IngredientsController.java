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

import org.adhuc.cena.menu.ingredients.IngredientConsultationAppService;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientManagementAppService;

/**
 * A REST controller exposing /api/ingredients resource.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Validated
@RestController
@ExposesResourceFor(IngredientModel.class)
@RequestMapping(path = "/api/ingredients", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class IngredientsController {

    private final EntityLinks links;
    private final IngredientConsultationAppService ingredientConsultation;
    private final IngredientManagementAppService ingredientManagement;
    private final IngredientModelAssembler modelAssembler;

    /**
     * Gets the ingredient information for all ingredients.
     */
    @GetMapping
    @ResponseStatus(OK)
    CollectionModel<IngredientModel> getIngredients() {
        var ingredients = ingredientConsultation.getIngredients();
        return modelAssembler.toCollectionModel(ingredients);
    }

    /**
     * Creates an ingredient.
     */
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    ResponseEntity<Void> createIngredient(@RequestBody @Valid CreateIngredientRequest request, Errors errors) throws URISyntaxException {
        var identity = IngredientId.generate();
        ingredientManagement.createIngredient(request.toCommand(identity));
        return ResponseEntity.created(new URI(links.linkToItemResource(IngredientModel.class, identity).getHref())).build();
    }

}
