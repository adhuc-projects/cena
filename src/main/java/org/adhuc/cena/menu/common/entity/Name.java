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
package org.adhuc.cena.menu.common.entity;

import static org.adhuc.cena.menu.util.Assert.hasText;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * A name value definition. A name must contain a value, trimmed on creation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Value
@Accessors(fluent = true)
public class Name {

    @NonNull
    private final String value;

    public Name(String value) {
        hasText(value, "Cannot define name with invalid value");
        this.value = capitalize(value.strip());
    }

    private String capitalize(String value) {
        if (value.length() == 1) {
            return value.toUpperCase();
        }
        char[] charArray = value.toCharArray();
        charArray[0] = Character.toUpperCase(charArray[0]);
        return new String(charArray);
    }

    /**
     * Indicates whether this name's value is equal to the specified one's value, ignoring case.
     *
     * @param other the name to compare this one to.
     * @return {@code true} if values are equal, ignoring case, {@code false} otherwise.
     */
    public boolean equalsIgnoreCase(@NonNull Name other) {
        return value.equalsIgnoreCase(other.value);
    }

    public String toString() {
        return value;
    }

}
