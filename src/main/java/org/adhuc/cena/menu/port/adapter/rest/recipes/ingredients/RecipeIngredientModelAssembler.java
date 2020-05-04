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

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.port.adapter.rest.ingredients.IngredientModel;
import org.adhuc.cena.menu.port.adapter.rest.recipes.RecipeModel;
import org.adhuc.cena.menu.recipes.RecipeIngredient;

/**
 * A {@link org.springframework.hateoas.server.RepresentationModelAssembler RepresentationModelAssembler} implementation
 * allowing building {@link RecipeIngredientModel}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Component
public class RecipeIngredientModelAssembler extends RepresentationModelAssemblerSupport<RecipeIngredient, RecipeIngredientModel> {

    private static final String RECIPE_LINK = "recipe";
    private static final String INGREDIENT_LINK = "ingredient";

    private EntityLinks links;

    /**
     * Creates a model assembler for recipe ingredients.
     */
    RecipeIngredientModelAssembler(EntityLinks links) {
        super(RecipeIngredientsController.class, RecipeIngredientModel.class);
        this.links = links;
    }

    @Override
    public RecipeIngredientModel toModel(RecipeIngredient recipeIngredient) {
        return instantiateModel(recipeIngredient)
                .add(links.linkFor(RecipeIngredientModel.class, recipeIngredient.recipeId())
                        .slash(recipeIngredient.ingredientId()).withSelfRel())
                .add(links.linkToItemResource(RecipeModel.class, recipeIngredient.recipeId()).withRel(RECIPE_LINK))
                .add(links.linkToItemResource(IngredientModel.class, recipeIngredient.ingredientId()).withRel(INGREDIENT_LINK));
    }

    public CollectionModel<RecipeIngredientModel> toCollectionModel(String recipeId, Iterable<? extends RecipeIngredient> recipeIngredients) {
        return super.toCollectionModel(recipeIngredients)
                .add(links.linkFor(RecipeIngredientModel.class, recipeId).withSelfRel())
                .add(links.linkToItemResource(RecipeModel.class, recipeId).withRel(RECIPE_LINK));
    }

    @Override
    protected RecipeIngredientModel instantiateModel(RecipeIngredient recipeIngredient) {
        return new RecipeIngredientModel(recipeIngredient);
    }

}
