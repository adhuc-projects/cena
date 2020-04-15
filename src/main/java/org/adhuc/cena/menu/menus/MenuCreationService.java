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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.AlreadyExistingEntityException;

/**
 * An domain service dedicated to menu creation. This service ensures that a menu can be created only if its identity is
 * not already used.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class MenuCreationService {

    @NonNull
    private MenuRepository repository;

    /**
     * Creates a menu, ensuring the identity is not already used.
     *
     * @param command the menu creation command.
     * @return the created menu.
     * @throws AlreadyExistingEntityException if a menu already exists with the identity specified in creation command.
     */
    Menu createMenu(CreateMenu command) {
        if (repository.exists(command.menuId())) {
            throw new AlreadyExistingEntityException(Menu.class, command.menuId());
        }
        return repository.save(new Menu(command));
    }

}
