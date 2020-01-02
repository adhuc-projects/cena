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

import static java.util.stream.Collectors.toList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import org.adhuc.cena.menu.ingredients.Ingredient;

/**
 * A REST resource encapsulating ingredient information.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@ToString(callSuper = true)
@Relation(collectionRelation = "data")
@JsonInclude(NON_EMPTY)
public class IngredientModel extends RepresentationModel<IngredientModel> {

    @NonNull
    @JsonProperty("id")
    private final String ingredientId;
    @NonNull
    @JsonProperty("name")
    private final String ingredientName;
    @JsonProperty("quantityTypes")
    private final List<String> ingredientQuantityTypes;

    IngredientModel(@NonNull Ingredient ingredient) {
        ingredientId = ingredient.id().toString();
        ingredientName = ingredient.name();
        ingredientQuantityTypes = ingredient.quantityTypes().stream().map(Enum::toString).collect(toList());
    }

}
