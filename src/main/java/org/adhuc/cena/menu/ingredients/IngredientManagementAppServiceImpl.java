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
package org.adhuc.cena.menu.ingredients;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.security.AsIngredientManager;
import org.adhuc.cena.menu.common.security.AsSuperAdministrator;

/**
 * An {@link IngredientManagementAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Slf4j
@Service
@AsIngredientManager
@RequiredArgsConstructor
class IngredientManagementAppServiceImpl implements IngredientManagementAppService {

    @NonNull
    private IngredientCreationService ingredientCreationService;
    @NonNull
    private IngredientDeletionService ingredientDeletionService;

    @Override
    public void createIngredient(@NonNull CreateIngredient command) {
        log.info("Create ingredient from command {}", command);
        ingredientCreationService.createIngredient(command);
    }

    @Override
    @AsSuperAdministrator
    public void deleteIngredients() {
        ingredientDeletionService.deleteIngredients();
    }

    @Override
    public void deleteIngredient(@NonNull DeleteIngredient command) {
        ingredientDeletionService.deleteIngredient(command);
    }

}
