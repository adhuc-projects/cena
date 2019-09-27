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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.BasicEntity;

/**
 * An ingredient definition.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true)
public class Ingredient extends BasicEntity<IngredientId> {

    @NonNull
    private String name;

    /**
     * Creates an ingredient based on the specified creation command.
     *
     * @param command the ingredient creation command.
     */
    public Ingredient(@NonNull CreateIngredient command) {
        this(command.ingredientId(), command.ingredientName());
    }

    /**
     * Creates an ingredient.
     *
     * @param id   the ingredient identity.
     * @param name the ingredient name.
     */
    public Ingredient(@NonNull IngredientId id, @NonNull String name) {
        super(id);
        hasText(name, "Cannot set ingredient name with invalid value");
        this.name = name;
    }

}
