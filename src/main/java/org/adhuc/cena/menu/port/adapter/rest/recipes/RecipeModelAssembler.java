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

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients.RecipeIngredientModel;
import org.adhuc.cena.menu.recipes.Recipe;

/**
 * A {@link org.springframework.hateoas.server.RepresentationModelAssembler RepresentationModelAssembler} implementation
 * allowing building {@link RecipeModel}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Component
public class RecipeModelAssembler extends RepresentationModelAssemblerSupport<Recipe, RecipeModel> {

    private static final String RECIPE_INGREDIENTS_RELATION = "ingredients";

    private EntityLinks links;

    /**
     * Creates a model assembler for recipes.
     */
    RecipeModelAssembler(EntityLinks links) {
        super(RecipesController.class, RecipeModel.class);
        this.links = links;
    }

    @Override
    public RecipeModel toModel(Recipe recipe) {
        return instantiateModel(recipe)
                .add(links.linkToItemResource(RecipeModel.class, recipe.id()).withSelfRel())
                .add(links.linkFor(RecipeIngredientModel.class, recipe.id()).withRel(RECIPE_INGREDIENTS_RELATION));
    }

    @Override
    public CollectionModel<RecipeModel> toCollectionModel(Iterable<? extends Recipe> recipes) {
        return super.toCollectionModel(recipes)
                .add(links.linkToCollectionResource(RecipeModel.class).withSelfRel());
    }

    @Override
    protected RecipeModel instantiateModel(Recipe recipe) {
        return new RecipeModel(recipe);
    }

}

