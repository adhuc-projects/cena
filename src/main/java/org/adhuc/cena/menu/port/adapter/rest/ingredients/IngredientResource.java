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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.port.adapter.rest.support.HalResource;

/**
 * A REST resource encapsulating ingredient information.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
class IngredientResource extends HalResource {

    @NonNull
    @JsonProperty("id")
    private final String ingredientId;
    @NonNull
    @JsonProperty("name")
    private final String ingredientName;

    IngredientResource(@NonNull Ingredient ingredient) {
        ingredientId = ingredient.id().toString();
        ingredientName = ingredient.name();
    }

}
