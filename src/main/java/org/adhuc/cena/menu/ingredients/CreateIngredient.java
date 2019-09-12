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
package org.adhuc.cena.menu.ingredients;

import static org.springframework.util.Assert.hasText;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * An ingredient creation command.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Value
@Accessors(fluent = true)
public class CreateIngredient {

    private final IngredientId ingredientId;
    private final String ingredientName;

    /**
     * Creates an ingredient creation command.
     *
     * @param ingredientId the ingredient identity.
     * @param ingredientName the ingredient name.
     */
    public CreateIngredient(@NonNull IngredientId ingredientId, @NonNull String ingredientName) {
        hasText(ingredientName, "Cannot create ingredient creation command with invalid ingredient name");
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
    }

}
