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

/**
 * A domain service dedicated to ingredient deletion. This service ensures that an ingredient can be deleted only if not
 * related to another object.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class IngredientDeletionService {

    @NonNull
    private IngredientRelatedService ingredientRelatedRetriever;
    @NonNull
    private IngredientRepository repository;

    /**
     * Deletes the ingredients.
     *
     * @throws IngredientNotDeletableRelatedToObjectException if at least one ingredient is related to another object.
     */
    void deleteIngredients() {
        if (ingredientRelatedRetriever.areIngredientsRelated()) {
            throw new IngredientNotDeletableRelatedToObjectException(ingredientRelatedRetriever.relatedObjectName());
        }
        repository.deleteAll();
    }

    /**
     * Deletes an ingredient.
     *
     * @param command the ingredient deletion command.
     * @throws IngredientNotDeletableRelatedToObjectException if the ingredient is related to another object.
     */
    void deleteIngredient(@NonNull DeleteIngredient command) {
        if (ingredientRelatedRetriever.isIngredientRelated(command.ingredientId())) {
            throw new IngredientNotDeletableRelatedToObjectException(command.ingredientId(),
                    ingredientRelatedRetriever.relatedObjectName());
        }
        repository.delete(repository.findNotNullById(command.ingredientId()));
    }

}
