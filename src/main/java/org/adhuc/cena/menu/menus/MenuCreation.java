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

import static java.util.stream.Collectors.toList;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.adhuc.cena.menu.common.DomainService;
import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;
import org.adhuc.cena.menu.recipes.RecipeConsultation;

/**
 * A domain service dedicated to menu creation. This service ensures that a menu can be created only if its identity is
 * not already used and related recipes exist.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@RequiredArgsConstructor
@DomainService
class MenuCreation {

    @NonNull
    private MenuRepository menuRepository;
    @NonNull
    private RecipeConsultation recipeAppService;

    /**
     * Creates a menu, ensuring the identity is not already used and related recipes exist.
     *
     * @param command the menu creation command.
     * @throws AlreadyExistingEntityException             if a menu already exists with the identity specified in creation command.
     * @throws MenuNotCreatableWithUnknownRecipeException if at least one related recipe does not exist.
     */
    void createMenu(CreateMenu command) {
        ensureMenuDoesNotExist(command);
        ensureRecipesExist(command);
        menuRepository.save(new Menu(command));
    }

    private void ensureMenuDoesNotExist(CreateMenu command) {
        if (menuRepository.exists(command.menuId())) {
            throw new AlreadyExistingEntityException("Menu is already scheduled at " + command.menuId().toScheduleString());
        }
    }

    private void ensureRecipesExist(CreateMenu command) {
        var unknownRecipes = command.mainCourseRecipes().stream()
                .filter(recipeId -> !recipeAppService.exists(recipeId))
                .sorted()
                .collect(toList());
        if (!unknownRecipes.isEmpty()) {
            throw new MenuNotCreatableWithUnknownRecipeException(unknownRecipes, command.menuId());
        }
    }

}
