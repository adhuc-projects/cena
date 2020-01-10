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

import static org.adhuc.cena.menu.ingredients.MeasurementType.*;
import static org.adhuc.cena.menu.util.Assert.notNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.ingredients.MeasurementType;
import org.adhuc.cena.menu.util.Assert;

/**
 * The measurement units, used to determine the quantity of ingredients in recipes.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum MeasurementUnit {

    LITER(VOLUME),
    CENTILITER(VOLUME),
    MILLILITER(VOLUME),
    TABLESPOON(VOLUME),
    TEASPOON(VOLUME),
    GRAM(WEIGHT),
    KILOGRAM(WEIGHT),
    POUND(WEIGHT),
    UNIT(COUNT),
    TEN(COUNT),
    DOZEN(COUNT),
    PINCH(AT_CONVENIENCE),
    UNDEFINED(null);

    private final MeasurementType associatedType;

    /**
     * Indicates whether this measurement unit is associated to one of the specified measurement types.
     *
     * @param measurementTypes the measurement types to check for association.
     * @return {@code true} if measurement unit is associated to at least one of the specified measurement types,
     * {@code false} otherwise.
     */
    public boolean isAssociatedToOneOf(@NonNull Collection<MeasurementType> measurementTypes) {
        Assert.notEmpty(measurementTypes, () -> "Measurement types cannot be empty");
        return UNDEFINED.equals(this) || measurementTypes.stream().anyMatch(type -> associatedType.equals(type));
    }

    /**
     * Indicates whether this measurement unit is associated to one of the specified measurement types.
     *
     * @param measurementTypes the measurement types to check for association.
     * @return {@code true} if measurement unit is associated to at least one of the specified measurement types,
     * {@code false} otherwise.
     */
    public boolean isAssociatedToOneOf(MeasurementType... measurementTypes) {
        Arrays.stream(measurementTypes).forEach(type -> notNull(type, "Measurement type cannot be null"));
        return isAssociatedToOneOf(Set.of(measurementTypes));
    }

}
