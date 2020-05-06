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
package org.adhuc.cena.menu.ingredients;

import java.util.List;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.entity.Name;

/**
 * An ingredient creation command.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Value
@Accessors(fluent = true)
public class CreateIngredient {

    private final IngredientId ingredientId;
    private final Name ingredientName;
    private final List<MeasurementType> ingredientMeasurementTypes;

    /**
     * Creates an ingredient creation command.
     *
     * @param ingredientId               the ingredient identity.
     * @param ingredientName             the ingredient name.
     * @param ingredientMeasurementTypes the ingredient measurement types.
     */
    public CreateIngredient(@NonNull IngredientId ingredientId, @NonNull Name ingredientName,
                            @NonNull List<MeasurementType> ingredientMeasurementTypes) {
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.ingredientMeasurementTypes = List.copyOf(ingredientMeasurementTypes);
    }

}
