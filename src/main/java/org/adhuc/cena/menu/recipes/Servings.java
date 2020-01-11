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

import static org.adhuc.cena.menu.util.Assert.isTrue;

import lombok.Value;
import lombok.experimental.Accessors;

/**
 * A number of servings for a recipe definition. Value must be positive.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Value
@Accessors(fluent = true)
public class Servings {

    public static final Servings DEFAULT = new Servings(4);

    private final int value;

    public Servings(int value) {
        isTrue(value > 0, "Cannot create number of servings for a recipe with non positive value");
        this.value = value;
    }

}
