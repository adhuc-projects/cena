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
package org.adhuc.cena.menu.steps.serenity.menus;

import static org.assertj.core.api.Assertions.assertThat;

import net.serenitybdd.core.Serenity;

/**
 * A menu storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public final class MenuStorageDelegate {

    private static final String MENU_SESSION_KEY = "menu";

    /**
     * Stores the menu.
     *
     * @param menu the menu to store.
     * @return the menu.
     */
    public MenuValue storeMenu(MenuValue menu) {
        Serenity.setSessionVariable(MENU_SESSION_KEY).to(menu);
        return menu;
    }

    /**
     * Gets the stored menu, or fails if menu was not previously stored.
     *
     * @return the stored menu.
     * @throws AssertionError if menu was not previously stored.
     */
    public MenuValue storedMenu() {
        MenuValue menu = Serenity.sessionVariableCalled(MENU_SESSION_KEY);
        assertThat(menu).isNotNull();
        return menu;
    }

}
