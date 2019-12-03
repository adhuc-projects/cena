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

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.ingredients.Ingredient;

/**
 * A {@link org.springframework.hateoas.server.RepresentationModelAssembler RepresentationModelAssembler} implementation
 * allowing building {@link IngredientModel}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Component
class IngredientModelAssembler extends RepresentationModelAssemblerSupport<Ingredient, IngredientModel> {

    private EntityLinks links;

    /**
     * Creates a model assembler for ingredients.
     */
    IngredientModelAssembler(EntityLinks links) {
        super(IngredientsController.class, IngredientModel.class);
        this.links = links;
    }

    @Override
    public IngredientModel toModel(Ingredient ingredient) {
        return instantiateModel(ingredient)
            .add(links.linkToItemResource(Ingredient.class, ingredient.id()).withSelfRel());
    }

    @Override
    public CollectionModel<IngredientModel> toCollectionModel(Iterable<? extends Ingredient> entities) {
        return super.toCollectionModel(entities)
                .add(links.linkToCollectionResource(Ingredient.class).withSelfRel());
    }

    @Override
    protected IngredientModel instantiateModel(Ingredient ingredient) {
        return new IngredientModel(ingredient);
    }

}
