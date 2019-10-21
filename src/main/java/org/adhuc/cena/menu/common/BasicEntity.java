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
package org.adhuc.cena.menu.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * An abstract entity definition, containing the entity identity.
 *
 * @param <I> the identity type.
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Accessors(fluent = true)
public abstract class BasicEntity<I extends Identity> implements Entity<I> {

    @Getter
    @NonNull
    private final I id;

    @Override
    public String toString() {
        return String.format("id=%s", id);
    }
}
