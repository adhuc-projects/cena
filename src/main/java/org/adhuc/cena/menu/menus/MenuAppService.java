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
package org.adhuc.cena.menu.menus;

import java.util.List;

/**
 * An application service for menus.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public interface MenuAppService {

    /**
     * Gets the menus for the specified owner.
     *
     * @param owner the owner of the menus.
     * @return the menus (not modifiable).
     */
    List<Menu> getMenus(MenuOwner owner);

    /**
     * Gets the menu corresponding to the specified identity.
     *
     * @param menuId the menu identity.
     * @return the menu.
     * @throws org.adhuc.cena.menu.common.EntityNotFoundException if no menu corresponds to identity.
     */
    Menu getMenu(MenuId menuId);

    /**
     * Creates a menu.
     *
     * @param command the menu creation command.
     * @throws org.adhuc.cena.menu.common.AlreadyExistingEntityException if a menu with meal type already exists for the
     *                                                                   owner at specified date.
     */
    Menu createMenu(CreateMenu command);

}