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
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

import static org.adhuc.cena.menu.steps.serenity.support.resource.ApiClientResource.MENUS_LINK;

import java.time.LocalDate;
import java.util.function.Supplier;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;
import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * The menu deletion rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuDeletionSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();
    @Delegate
    private final MenuStorageDelegate menuStorage = new MenuStorageDelegate();
    private final MenuListClientDelegate listClient = new MenuListClientDelegate();

    @Step("Delete menu {0}")
    public MenuValue deleteMenu(MenuValue menu) {
        return deleteMenuOwnedBy(menu, this::rest);
    }

    @Step("Delete menu scheduled for {0}'s {1}")
    public MenuValue deleteMenu(LocalDate date, String mealType) {
        var menu = listClient.getFromMenusList(date, mealType);
        assertThat(menu).isPresent();
        return deleteMenuOwnedBy(menu.get(), this::rest);
    }

    @Step("Delete menu {0} owned by {1}")
    public MenuValue deleteMenuOwnedBy(MenuValue menu, AuthenticationType owner) {
        return deleteMenuOwnedBy(menu, () -> rest(owner));
    }

    private MenuValue deleteMenuOwnedBy(MenuValue menu, Supplier<RequestSpecification> requestSpecificationSupplier) {
        requestSpecificationSupplier.get().contentType(HAL_JSON_VALUE).delete(menu.selfLink()).andReturn();
        return menu;
    }

    @Step("Attempt deleting menu scheduled for {0}'s {1}")
    public void attemptDeletingMenu(LocalDate date, String mealType) {
        var menusLink = resourceUrlResolverDelegate.apiClientResource().maybeLink(MENUS_LINK);
        var expectedMenu = MenuValue.buildUnknownMenuValue(date, mealType,
                menusLink.orElseGet(resourceUrlResolverDelegate::notAccessibleMenusResourceUrl));

        // Menus list is not accessible for community users
        if (menusLink.isPresent()) {
            var existingMenu = listClient.getFromMenusList(date, mealType);
            assertThat(existingMenu).isNotPresent();
        }

        rest().delete(expectedMenu.selfLink());
    }

    @Step("Assert menu has been successfully deleted")
    public void assertMenuSuccessfullyDeleted() {
        assertNoContent();
    }

}
