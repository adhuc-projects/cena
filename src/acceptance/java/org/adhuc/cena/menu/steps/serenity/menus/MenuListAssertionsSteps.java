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

import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The menus list rest-service client steps definition dedicated to assertions.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuListAssertionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final MenuListClientDelegate listClient = new MenuListClientDelegate(
            resourceUrlResolverDelegate.menusResourceUrl());

    @Step("Assert menu {0} is in menus list")
    public void assertInMenusList(MenuValue menu) {
        assertThat(listClient.getFromMenusList(menu)).isPresent().get().isEqualTo(menu);
    }

    @Step("Assert menu {0} is not in menus list")
    public void assertNotInMenusList(MenuValue menu) {
        assertThat(listClient.getFromMenusList(menu)).isNotPresent();
    }

}
