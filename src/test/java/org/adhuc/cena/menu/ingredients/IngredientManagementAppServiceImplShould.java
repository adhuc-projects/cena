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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_MEASUREMENT_TYPES;

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.adhuc.cena.menu.common.aggregate.AlreadyExistingEntityException;
import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.common.aggregate.Name;

/**
 * The {@link IngredientManagementAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient management service should")
@ExtendWith(MockitoExtension.class)
class IngredientManagementAppServiceImplShould {

    private IngredientRepository ingredientRepository;
    private IngredientManagementAppServiceImpl service;
    @Mock
    private IngredientRelatedService ingredientRelatedService;

    @BeforeEach
    void setUp() {
        ingredientRepository = new InMemoryIngredientRepository();
        service = new IngredientManagementAppServiceImpl(new IngredientCreationService(ingredientRepository),
                new IngredientDeletionService(ingredientRelatedService, ingredientRepository));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when creating ingredient from null command")
    void throwIAECreateIngredientNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.createIngredient(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when deleting ingredient from null command")
    void throwIAEDeleteIngredientNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteIngredient(null));
    }

    @Nested
    @DisplayName("with no ingredient")
    class WithNoIngredient {

        @Test
        @DisplayName("retrieve ingredient with identity after creation")
        void retrieveIngredientWithIdAfterCreation() {
            var ingredient = ingredient();
            service.createIngredient(createCommand(ingredient));
            assertThat(ingredientRepository.findNotNullById(ingredient.id())).isNotNull().isEqualToComparingFieldByField(ingredient);
        }

    }

    @Nested
    @DisplayName("with tomato")
    class WithTomato {

        @BeforeEach
        void setUp() {
            ingredientRepository.save(ingredient());
        }

        @Test
        @DisplayName("create cucumber successfully")
        void createIngredient() {
            var ingredient = ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES);
            service.createIngredient(createCommand(ingredient));
            assertThat(ingredientRepository.findNotNullById(ingredient.id())).isNotNull().isEqualToComparingFieldByField(ingredient);
        }

        @ParameterizedTest
        @CsvSource({"Tomato", "tomato", "TOMATO"})
        @DisplayName("fail during duplicated tomato creation")
        void failCreatingDuplicateTomato(String name) {
            var exception = assertThrows(IngredientNameAlreadyUsedException.class,
                    () -> service.createIngredient(createCommand(ingredient(IngredientId.generate(), new Name(name), List.of()))));
            assertThat(exception).hasMessage("Ingredient name 'Tomato' already used by an existing ingredient");
        }

        @Test
        @DisplayName("throw AlreadyExistingEntityException when creating ingredient with already used identity")
        void throwAlreadyExistingEntityCreateRecipeAlreadyExistingId() {
            var exception = assertThrows(AlreadyExistingEntityException.class,
                    () -> service.createIngredient(createCommand(ingredient(ID, new Name("unused"), List.of()))));
            assertThat(exception.getMessage()).isEqualTo("Entity of type Ingredient with identity '" + ID + "' already exists");
        }

        @Test
        @DisplayName("delete tomato successfully when not used in a recipe")
        void deleteTomatoNotUsedInRecipe() {
            when(ingredientRelatedService.isIngredientRelated(ID)).thenReturn(false);
            assumeThat(ingredientRepository.exists(ID)).isTrue();
            service.deleteIngredient(deleteCommand());
            assertThat(ingredientRepository.exists(ID)).isFalse();
        }

        @Test
        @DisplayName("fail during tomato deletion when used in a recipe")
        void deleteTomatoUsedInRecipe() {
            when(ingredientRelatedService.isIngredientRelated(ID)).thenReturn(true);
            when(ingredientRelatedService.relatedObjectName()).thenReturn("recipe");
            assumeThat(ingredientRepository.exists(ID)).isTrue();
            var exception = assertThrows(IngredientNotDeletableRelatedToObjectException.class, () -> service.deleteIngredient(deleteCommand()));
            assertThat(exception.getMessage()).isEqualTo("Ingredient '" + ID + "' cannot be deleted as it is related to at least one recipe");
        }

        @Test
        @DisplayName("throw EntityNotFoundException when deleting unknown ingredient")
        void throwEntityNotFoundDeleteUnknownIngredient() {
            var exception = assertThrows(EntityNotFoundException.class, () -> service.deleteIngredient(deleteCommand(CUCUMBER_ID)));
            assertThat(exception.getIdentity()).isEqualTo(CUCUMBER_ID.toString());
        }

    }

}
