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
package org.adhuc.cena.menu.recipes;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

import org.adhuc.cena.menu.common.aggregate.UuidIdentity;

/**
 * A recipe identity.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@EqualsAndHashCode(callSuper = true)
public class RecipeId extends UuidIdentity {

    /**
     * Creates a recipe identity with the specified value.
     *
     * @param id the identity value.
     */
    public RecipeId(@NonNull String id) {
        this(parseUUID(Recipe.class, id));
    }

    RecipeId(@NonNull UUID id) {
        super(id);
    }

    /**
     * Generates a new recipe identity.
     *
     * @return a new recipe identity.
     */
    public static RecipeId generate() {
        return new RecipeId(UUID.randomUUID());
    }

}
