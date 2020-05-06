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

import java.util.List;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.NonNull;
import lombok.ToString;

import org.adhuc.cena.menu.common.entity.Name;
import org.adhuc.cena.menu.ingredients.CreateIngredient;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.MeasurementType;

/**
 * A request to create an ingredient.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CreateIngredientRequest {

    @NotBlank
    private String name;
    private List<MeasurementType> measurementTypes;

    /**
     * Converts this request to a {@code CreateIngredient} command.
     *
     * @param id the ingredient identity.
     * @return the ingredient creation command.
     */
    CreateIngredient toCommand(@NonNull IngredientId id) {
        return new CreateIngredient(id, new Name(name), measurementTypes != null ? measurementTypes : List.of());
    }

}
