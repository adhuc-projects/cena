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

import java.util.Collection;
import java.util.Optional;

import lombok.NonNull;

import org.adhuc.cena.menu.common.EntityNotFoundException;

/**
 * A {@link Menu} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public interface MenuRepository {

    /**
     * Finds all the menus stored in the repository owned by the specified owner.
     *
     * @param owner the menu owner to filter on.
     * @return the menus owned by the owner.
     */
    Collection<Menu> findByOwner(MenuOwner owner);

    /**
     * Indicates whether a menu exists with the specified identity.
     *
     * @param menuId the menu identity.
     * @return {@code true} if menu exists with identity, {@code false} otherwise.
     */
    boolean exists(MenuId menuId);

    /**
     * Finds the menu corresponding to the specified identity.
     *
     * @param menuId the menu identity.
     * @return the menu if existing, empty otherwise.
     */
    Optional<Menu> findById(MenuId menuId);

    /**
     * Finds the menu corresponding to the specified identity.
     *
     * @param menuId the menu identity.
     * @return the menu if existing.
     * @throws EntityNotFoundException if no menu could be found for identity.
     */
    default Menu findNotNullById(@NonNull MenuId menuId) {
        var menu = findById(menuId);
        if (menu.isPresent()) {
            return menu.get();
        }
        throw new EntityNotFoundException(Menu.class, menuId);
    }

    /**
     * Saves the specified menu.
     *
     * @param menu the menu to save.
     * @return the saved menu.
     */
    <S extends Menu> S save(S menu);

    /**
     * Deletes all the menus stored in the repository.
     */
    void deleteAll();

    /**
     * Deletes the specified menu.
     *
     * @param menu the menu to delete.
     */
    void delete(Menu menu);

}
