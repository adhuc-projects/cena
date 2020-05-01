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
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import static org.adhuc.cena.menu.steps.serenity.support.resource.ApiClientResource.MENUS_LINK;

import java.time.LocalDate;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The menu detail rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuDetailSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final MenuStorageDelegate menuStorage = new MenuStorageDelegate();
    private final MenuListClientDelegate listClient = new MenuListClientDelegate();
    private final ResourceUrlResolverDelegate resourceUrlResolver = new ResourceUrlResolverDelegate();

    @Step("Get menu from {0}")
    public MenuValue getMenuFromUrl(String menuDetailUrl) {
        rest().accept(HAL_JSON_VALUE).get(menuDetailUrl).andReturn();
        return assertOk().extract().as(MenuValue.class);
    }

    @Step("Retrieve menu scheduled for {0}'s {1}")
    public MenuValue retrieveMenu(LocalDate date, String mealType) {
        var menu = listClient.getFromMenusList(date, mealType);
        return menu.orElseGet(() -> fail("Unable to retrieve menu scheduled for " + date + "'s " + mealType));
    }

    @Step("Attempt retrieving menu scheduled for {0}'s {1}")
    public void attemptRetrievingMenu(LocalDate date, String mealType) {
        var menusLink = resourceUrlResolver.apiClientResource().maybeLink(MENUS_LINK);
        var original = MenuValue.buildUnknownMenuValue(date, mealType,
                menusLink.orElseGet(resourceUrlResolver::notAccessibleMenusResourceUrl));
        // Menus list is not accessible for community users
        if (menusLink.isPresent()) {
            var menu = listClient.getFromMenusList(date, mealType);
            assertThat(menu).isNotPresent();
        }
        fetchMenu(original.selfLink());
    }

    @Step("Assert menu {0} is accessible")
    public void assertMenuInfoIsAccessible(MenuValue expected) {
        var actual = getMenuFromUrl(expected.selfLink());
        assertThat(actual).isEqualTo(expected);
    }

    private void fetchMenu(String menuDetailUrl) {
        rest().accept(HAL_JSON_VALUE).get(menuDetailUrl).andReturn();
    }

}
