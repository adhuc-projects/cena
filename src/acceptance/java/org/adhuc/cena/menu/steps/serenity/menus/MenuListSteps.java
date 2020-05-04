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

import java.time.LocalDate;
import java.util.Collection;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;

/**
 * The menus list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuListSteps {

    private final RestClientDelegate restClient = new RestClientDelegate();
    private final ResourceUrlResolverDelegate resourceUrlResolver = new ResourceUrlResolverDelegate();
    private final MenuListClientDelegate listClient = new MenuListClientDelegate();
    @Delegate
    private final MenuStorageDelegate menuStorage = new MenuStorageDelegate();

    @Step("Get menus list")
    public Collection<MenuValue> getMenus() {
        return listClient.fetchMenus();
    }

    @Step("Get menus list between {0} and {1}")
    public Collection<MenuValue> getMenus(LocalDate since, LocalDate until) {
        return listClient.fetchMenus(since, until);
    }

    @Step("Attempt getting menus list")
    public void attemptGetMenus() {
        restClient.rest().get(resourceUrlResolver.notAccessibleMenusResourceUrl()).andReturn();
    }

}
