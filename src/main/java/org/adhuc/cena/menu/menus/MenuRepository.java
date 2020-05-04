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
import java.util.Collection;

import org.adhuc.cena.menu.common.Repository;

/**
 * A {@link Menu} repository.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public interface MenuRepository extends Repository<Menu, MenuId> {

    @Override
    default Class<Menu> entityType() {
        return Menu.class;
    }

    /**
     * Finds all the menus stored in the repository owned by the specified owner.
     *
     * @param owner the menu owner to filter on.
     * @return the menus owned by the owner.
     */
    Collection<Menu> findByOwner(MenuOwner owner);

    /**
     * Finds all the menus stored in the repository owned by the specified owner, whose date are between the specified
     * range inclusively.
     *
     * @param owner the menu owner to filter on.
     * @param since the inclusive lower bound date to filter menus on.
     * @param until the inclusive upper bound date to filter menus on.
     * @return the menus owned by the owner.
     */
    Collection<Menu> findByOwnerAndDateBetween(MenuOwner owner, LocalDate since, LocalDate until);

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
