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

import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.adhuc.cena.menu.common.entity.AlreadyExistingEntityException;
import org.adhuc.cena.menu.common.entity.EntityNotFoundException;
import org.adhuc.cena.menu.common.entity.Name;

/**
 * The {@link IngredientAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient service should")
@ExtendWith(MockitoExtension.class)
class IngredientAppServiceImplShould {

    private IngredientRepository ingredientRepository;
    private IngredientAppServiceImpl service;
    @Mock
    private IngredientRelatedService ingredientRelatedService;

    @BeforeEach
    void setUp() {
        ingredientRepository = new InMemoryIngredientRepository();
        service = new IngredientAppServiceImpl(new IngredientCreationService(ingredientRepository),
                new IngredientDeletionService(ingredientRelatedService, ingredientRepository), ingredientRepository);
    }

    @Test
    @DisplayName("return unmodifiable list of ingredients")
    void returnUnmodifiableListOfIngredients() {
        assertThrows(UnsupportedOperationException.class, () -> service.getIngredients().add(ingredient()));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting ingredient from null identity")
    void throwIAEGetIngredientNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getIngredient(null));
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
        @DisplayName("return empty list of ingredients")
        void returnEmptyIngredientList() {
            assertThat(service.getIngredients()).isEmpty();
        }

        @Test
        @DisplayName("create ingredient successfully")
        void createIngredient() {
            var ingredient = ingredient();
            var created = service.createIngredient(createCommand(ingredient));
            assertThat(created).isNotNull().isEqualToComparingFieldByField(ingredient);
        }

        @Test
        @DisplayName("retrieve ingredient with identity after creation")
        void retrieveIngredientWithIdAfterCreation() {
            var ingredient = ingredient();
            service.createIngredient(createCommand(ingredient));
            assertThat(service.getIngredient(ingredient.id())).isNotNull().isEqualToComparingFieldByField(ingredient);
        }

    }

    @Nested
    @DisplayName("with tomato")
    class WithTomato {

        private Ingredient tomato;

        @BeforeEach
        void setUp() {
            tomato = ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES);
            ingredientRepository.save(tomato);
        }

        @Test
        @DisplayName("return list of ingredients containing tomato")
        void returnIngredientListWithTomato() {
            assertThat(service.getIngredients()).isNotEmpty().usingFieldByFieldElementComparator()
                    .containsExactly(tomato);
        }

        @Test
        @DisplayName("throw EntityNotFoundException when getting ingredient from unknown identity")
        void throwEntityNotFoundExceptionUnknownId() {
            assertThrows(EntityNotFoundException.class, () -> service.getIngredient(CUCUMBER_ID));
        }

        @Test
        @DisplayName("return tomato when getting ingredient from tomato id")
        void returnTomato() {
            assertThat(service.getIngredient(TOMATO_ID)).isEqualToComparingFieldByField(tomato);
        }

        @Test
        @DisplayName("create cucumber successfully")
        void createIngredient() {
            var ingredient = ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES);
            var createdIngredient = service.createIngredient(createCommand(ingredient));
            assertThat(createdIngredient).isEqualToComparingFieldByField(ingredient);
            assertThat(service.getIngredient(ingredient.id())).isNotNull().isEqualToComparingFieldByField(ingredient);
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
            assumeThat(service.getIngredient(ID)).isNotNull();
            service.deleteIngredient(deleteCommand());
            assertThrows(EntityNotFoundException.class, () -> service.getIngredient(ID));
        }

        @Test
        @DisplayName("fail during tomato deletion when used in a recipe")
        void deleteTomatoUsedInRecipe() {
            when(ingredientRelatedService.isIngredientRelated(ID)).thenReturn(true);
            when(ingredientRelatedService.relatedObjectName()).thenReturn("recipe");
            assumeThat(service.getIngredient(ID)).isNotNull();
            var exception = assertThrows(IngredientNotDeletableRelatedToObjectException.class, () -> service.deleteIngredient(deleteCommand()));
            assertThat(exception.getMessage()).isEqualTo("Ingredient '" + ID + "' cannot be deleted as it is related to at least one recipe");
        }

        @Test
        @DisplayName("throw EntityNotFoundException when deleting unknown ingredient")
        void throwEntityNotFoundDeleteUnknownIngredient() {
            var exception = assertThrows(EntityNotFoundException.class, () -> service.deleteIngredient(deleteCommand(CUCUMBER_ID)));
            assertThat(exception.getIdentity()).isEqualTo(CUCUMBER_ID.toString());
        }

        @Nested
        @DisplayName("and cucumber")
        class AndCucumber {

            private Ingredient cucumber;

            @BeforeEach
            void setUp() {
                cucumber = ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES);
                ingredientRepository.save(cucumber);
            }

            @Test
            @DisplayName("return list containing all ingredients")
            void returnIngredientListWithAllIngredients() {
                assertThat(service.getIngredients()).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomato, cucumber);
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
                assertThat(service.getIngredients()).isEmpty();
            }

        }

    }

}
