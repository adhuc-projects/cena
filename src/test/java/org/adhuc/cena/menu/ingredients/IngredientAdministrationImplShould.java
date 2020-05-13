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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The {@link IngredientAdministrationImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient administration service should")
@ExtendWith(MockitoExtension.class)
class IngredientAdministrationImplShould {

    private IngredientRepository ingredientRepository;
    private IngredientAdministrationImpl service;
    @Mock
    private IngredientRelatedService ingredientRelatedService;

    @BeforeEach
    void setUp() {
        ingredientRepository = new InMemoryIngredientRepository();
        service = new IngredientAdministrationImpl(new IngredientDeletion(ingredientRelatedService, ingredientRepository));

        ingredientRepository.save(ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES));
        ingredientRepository.save(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));
    }

    @Test
    @DisplayName("fail during ingredients deletion when at least one ingredient is used in recipe")
    void deleteIngredientsUsedInRecipe() {
        when(ingredientRelatedService.areIngredientsRelated()).thenReturn(true);
        when(ingredientRelatedService.relatedObjectName()).thenReturn("recipe");
        var exception = assertThrows(IngredientNotDeletableRelatedToObjectException.class, () -> service.deleteIngredients());
        assertThat(exception.getMessage()).isEqualTo("Ingredients cannot be deleted as at least one is related to at least one recipe");
    }

    @Test
    @DisplayName("return empty list after deleting all ingredients")
    void returnEmptyListAfterDeletion() {
        when(ingredientRelatedService.areIngredientsRelated()).thenReturn(false);
        service.deleteIngredients();
        assertThat(ingredientRepository.findAll()).isEmpty();
    }

}
