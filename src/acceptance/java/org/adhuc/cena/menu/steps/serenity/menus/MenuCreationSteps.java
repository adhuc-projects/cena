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
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpHeaders.LOCATION;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The menu creation rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuCreationSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final MenuStorageDelegate menuStorage = new MenuStorageDelegate();

    @Step("Create the menu {0}")
    public MenuValue createMenu(MenuValue menu) {
        rest().contentType(HAL_JSON_VALUE).body(menu).post(menusResourceUrl()).andReturn();
        return menu;
    }

    @Step("Try to create the menu {0}")
    public void tryToCreateMenu(MenuValue menu) {
        rest().contentType(HAL_JSON_VALUE).body(menu).post(notAccessibleMenusResourceUrl()).andReturn();
    }

    public void assertMenuSuccessfullyCreated(MenuValue menu) {
        var menuLocation = assertCreated().extract().header(LOCATION);
        assertThat(menuLocation).isNotBlank();
    }
}