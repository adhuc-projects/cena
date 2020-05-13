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

import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;

/**
 * An application service for menus management. Menu management provides command methods for menu manipulation and is
 * available only for authenticated users that are menu owners.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public interface MenuManagementAppService {

    /**
     * Creates a menu.
     *
     * @param command the menu creation command.
     * @throws AlreadyExistingEntityException             if a menu with meal type already exists for the owner at specified date.
     * @throws MenuNotCreatableWithUnknownRecipeException if at least one related recipe does not exist.
     */
    void createMenu(CreateMenu command);

    /**
     * Deletes a menu.
     *
     * @param command the menu deletion command.
     */
    void deleteMenu(DeleteMenu command);

}
