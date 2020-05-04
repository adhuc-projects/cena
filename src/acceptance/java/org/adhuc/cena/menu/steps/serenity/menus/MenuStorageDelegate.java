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
package org.adhuc.cena.menu.steps.serenity.menus;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import net.serenitybdd.core.Serenity;

/**
 * A menu storage manager.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public final class MenuStorageDelegate {

    private static final String MENUS_SESSION_KEY = "menus";
    private static final String MENU_SESSION_KEY = "menu";

    /**
     * Stores the menus.
     *
     * @param menus the menus to store.
     * @return the menus.
     */
    public Collection<MenuValue> storeMenus(Collection<MenuValue> menus) {
        return storeMenus(MENUS_SESSION_KEY, menus);
    }

    /**
     * Stores the menus.
     *
     * @param sessionKey the session key.
     * @param menus      the menus to store.
     * @return the menus.
     */
    Collection<MenuValue> storeMenus(String sessionKey, Collection<MenuValue> menus) {
        Serenity.setSessionVariable(sessionKey).to(menus);
        return menus;
    }

    /**
     * Gets the stored menus, or fails if menus were not previously stored.
     *
     * @return the stored menus.
     * @throws AssertionError if menus were not previously stored.
     */
    public Collection<MenuValue> storedMenus() {
        return storedMenus(MENUS_SESSION_KEY);
    }

    /**
     * Gets the stored menus, or fails if menus were not previously stored.
     *
     * @param sessionKey the session key.
     * @return the stored menus.
     * @throws AssertionError if menus were not previously stored.
     */
    Collection<MenuValue> storedMenus(String sessionKey) {
        assertThat(Serenity.hasASessionVariableCalled(sessionKey))
                .as("Menus in session (%s) must have been set previously", sessionKey).isTrue();
        return Serenity.sessionVariableCalled(sessionKey);
    }

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
