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

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.util.Assert;

/**
 * A menu owner definition. Owner is the only user (except super administrators) that will be able to access menus
 * created by himself.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Value
@Accessors(fluent = true)
public class MenuOwner {

    private String ownerName;

    public MenuOwner(@NonNull String ownerName) {
        Assert.hasText(ownerName, "Cannot create menu owner with invalid name");
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return this.ownerName;
    }

}
