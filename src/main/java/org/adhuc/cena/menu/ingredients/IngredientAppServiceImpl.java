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
package org.adhuc.cena.menu.ingredients;

import static org.adhuc.cena.menu.common.security.RolesDefinition.HAS_INGREDIENT_MANAGER_ROLE_PREDICATE;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * An {@link IngredientAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IngredientAppServiceImpl implements IngredientAppService {

    @NonNull
    private IngredientCreationService ingredientCreationService;
    @NonNull
    private IngredientRepository repository;

    @Override
    public List<Ingredient> getIngredients() {
        return List.copyOf(repository.findAll());
    }

    @Override
    public Ingredient getIngredient(@NonNull IngredientId ingredientId) {
        return repository.findNotNullById(ingredientId);
    }

    @Override
    @PreAuthorize(HAS_INGREDIENT_MANAGER_ROLE_PREDICATE)
    public Ingredient createIngredient(@NonNull CreateIngredient command) {
        log.info("Create ingredient from command {}", command);
        return ingredientCreationService.createIngredient(command);
    }

    @Override
    public void deleteIngredients() {
        repository.deleteAll();
    }

    @Override
    public void deleteIngredient(@NonNull DeleteIngredient command) {
        repository.delete(repository.findNotNullById(command.ingredientId()));
    }

}
