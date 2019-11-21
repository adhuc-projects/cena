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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.recipes.Recipe;

/**
 * A {@link org.springframework.hateoas.server.RepresentationModelAssembler RepresentationModelAssembler} implementation
 * allowing building {@link RecipeModel}s.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
class RecipeModelAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeModel> {

    /**
     * Creates a model assembler for recipes.
     */
    RecipeModelAssembler() {
        super(RecipesController.class, RecipeModel.class);
    }

    @Override
    public RecipeModel toModel(Recipe recipe) {
        return createModelWithId(recipe.id().toString(), recipe);
    }

    @Override
    protected RecipeModel instantiateModel(Recipe recipe) {
        return new RecipeModel(recipe);
    }

}

