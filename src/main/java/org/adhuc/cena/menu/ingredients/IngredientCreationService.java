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

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * An domain service dedicated to ingredient creation. This service ensures that an ingredient can be created only if its
 * name is not already used.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IngredientCreationService {

    @NonNull
    private final IngredientRepository repository;

    /**
     * Creates an ingredient, ensuring the ingredient name is not already used.
     *
     * @param command the ingredient creation command.
     * @return the created ingredient.
     * @throws IngredientNameAlreadyUsedException if the ingredient name specified in creation command is already used
     *                                            by another ingredient.
     */
    Ingredient createIngredient(CreateIngredient command) {
        if (repository.findByName(command.ingredientName()).isPresent()) {
            throw new IngredientNameAlreadyUsedException(command.ingredientName());
        }
        return repository.save(new Ingredient(command));
    }

}
