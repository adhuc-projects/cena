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

import lombok.NonNull;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.ingredients.Ingredient;

/**
 * A {@link org.springframework.hateoas.ResourceAssembler ResourceAssembler} implementation allowing building
 * {@link IngredientResource}s.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
public class IngredientResourceAssembler extends ResourceAssemblerSupport<Ingredient, IngredientResource> {

    /**
     * Creates a resource assembler for ingredients.
     */
    IngredientResourceAssembler() {
        super(IngredientsController.class, IngredientResource.class);
    }

    @Override
    public IngredientResource toResource(@NonNull Ingredient ingredient) {
        return createResourceWithId(ingredient.id().toString(), ingredient);
    }

    @Override
    protected IngredientResource instantiateResource(Ingredient ingredient) {
        return new IngredientResource(ingredient);
    }

}
