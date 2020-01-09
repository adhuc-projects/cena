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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import org.adhuc.cena.menu.recipes.Quantity;
import org.adhuc.cena.menu.recipes.RecipeIngredient;

/**
 * A REST resource encapsulating recipe ingredient information.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@ToString(callSuper = true)
@Relation(collectionRelation = "data")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class RecipeIngredientModel extends RepresentationModel<RecipeIngredientModel> {

    @NonNull
    @JsonProperty("id")
    private final String ingredientId;
    private final Integer quantity;
    private final String measurementUnit;

    RecipeIngredientModel(@NonNull RecipeIngredient recipeIngredient) {
        this.ingredientId = recipeIngredient.ingredientId().toString();
        var quantity = recipeIngredient.quantity();
        this.quantity = Quantity.UNDEFINED.equals(quantity) ? null : quantity.value();
        this.measurementUnit = Quantity.UNDEFINED.equals(quantity) ? null : quantity.unit().name();
    }

}
