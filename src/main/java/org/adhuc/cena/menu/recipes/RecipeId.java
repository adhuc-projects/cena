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
package org.adhuc.cena.menu.recipes;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.Identity;

/**
 * A recipe identity.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Accessors(fluent = true)
public class RecipeId implements Identity {

    private static AtomicInteger SEQUENCE = new AtomicInteger(0);

    @Getter
    private final int id;

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    /**
     * Generates a new recipe identity.
     *
     * @return a new recipe identity.
     */
    public static RecipeId generate() {
        return new RecipeId(SEQUENCE.incrementAndGet());
    }

    // For testing purpose only
    // TODO remove RecipeId.reset()
    public static void reset() {
        SEQUENCE = new AtomicInteger(0);
    }

}
