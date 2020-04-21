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

import static org.assertj.core.api.Assumptions.assumeThat;

import java.time.LocalDate;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The menus list rest-service client steps definition dedicated to assumptions.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuListAssumptionsSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final MenuListClientDelegate listClient = new MenuListClientDelegate(
            resourceUrlResolverDelegate.menusResourceUrl());

    @Steps
    private MenuCreationSteps menuCreation;
    @Steps
    private MenuDeletionSteps menuDeletion;

    @Step("Assume menu {0} is in menus list")
    public void assumeInMenusList(MenuValue menu) {
        if (listClient.getFromMenusList(menu).isEmpty()) {
            menuCreation.createMenu(menu);
        }
        assumeThat(listClient.fetchMenus()).anyMatch(m -> m.hasSameScheduleAs(menu));
    }

    @Step("Assume menu is not in menus list for date {0} and meal type {1}")
    public void assumeNotInMenusList(LocalDate date, String mealType) {
        listClient.getFromMenusList(date, mealType).ifPresent(m -> {
            menuDeletion.deleteMenu(m);
        });
        assumeThat(listClient.fetchMenus()).noneMatch(m -> m.isScheduledAt(date, mealType));
    }

}