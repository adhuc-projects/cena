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

import static org.adhuc.cena.menu.util.Assert.hasText;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.BasicEntity;

/**
 * An ingredient definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Ingredient extends BasicEntity<IngredientId> {

    @Getter
    @NonNull
    private String name;
    @Getter
    @NonNull
    private List<QuantityType> quantityTypes;

    /**
     * Creates an ingredient based on the specified creation command.
     *
     * @param command the ingredient creation command.
     */
    public Ingredient(@NonNull CreateIngredient command) {
        this(command.ingredientId(), command.ingredientName(), command.ingredientQuantityTypes());
    }

    /**
     * Creates an ingredient.
     *
     * @param id the ingredient identity.
     * @param name the ingredient name.
     * @param quantityTypes the ingredient quantity types.
     */
    Ingredient(@NonNull IngredientId id, @NonNull String name, @NonNull List<QuantityType> quantityTypes) {
        super(id);
        hasText(name, "Cannot set ingredient name with invalid value");
        this.name = name;
        this.quantityTypes = quantityTypes;
    }

}
