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

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.util.Assert;

/**
 * A quantity definition. Quantity value must be positive.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Value
@Accessors(fluent = true)
public class Quantity {

    /**
     * A quantity definition used when no quantity has been explicitly defined for an ingredient in a recipe.
     */
    public static final Quantity UNDEFINED = new Quantity(1, MeasurementUnit.UNDEFINED);

    private final int value;
    @NonNull
    private final MeasurementUnit unit;

    public Quantity(int value, @NonNull MeasurementUnit unit) {
        Assert.isTrue(value > 0, () -> "Cannot create quantity with negative or zero value");
        this.value = value;
        this.unit = unit;
    }

    public String toString() {
        return String.format("%d %s", value, unit.name().toLowerCase());
    }

}
