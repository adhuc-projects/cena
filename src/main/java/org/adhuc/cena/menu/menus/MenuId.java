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
package org.adhuc.cena.menu.menus;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.aggregate.Identity;

/**
 * A menu identity.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode
public class MenuId implements Identity, OwnedBy {

    @NonNull
    @Getter(AccessLevel.PUBLIC)
    private final MenuOwner owner;
    @NonNull
    private final LocalDate date;
    @NonNull
    private final MealType mealType;

    @Override
    public String toString() {
        return "owner=" + owner + ", date=" + date + ", mealType=" + mealType;
    }

    public String toScheduleString() {
        return date + "'s " + mealType.toString().toLowerCase();
    }

}
