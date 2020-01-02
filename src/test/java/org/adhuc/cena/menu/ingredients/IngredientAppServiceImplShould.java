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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryIngredientRepository;

/**
 * The {@link IngredientAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient service should")
class IngredientAppServiceImplShould {

    private IngredientRepository ingredientRepository;
    private IngredientAppServiceImpl service;

    @BeforeEach
    void setUp() {
        ingredientRepository = new InMemoryIngredientRepository();
        service = new IngredientAppServiceImpl(new IngredientCreationService(ingredientRepository), ingredientRepository);
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
            tomato = ingredient(TOMATO_ID, TOMATO, TOMATO_QUANTITY_TYPES);
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
            var ingredient = ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_QUANTITY_TYPES);
            var createdIngredient = service.createIngredient(createCommand(ingredient));
            assertThat(createdIngredient).isEqualToComparingFieldByField(ingredient);
            assertThat(service.getIngredient(ingredient.id())).isNotNull().isEqualToComparingFieldByField(ingredient);
        }

        @Test
        @DisplayName("fail during duplicated tomato creation")
        void failCreatingDuplicateTomato() {
            var exception = assertThrows(IngredientNameAlreadyUsedException.class, () -> service.createIngredient(createCommand(tomato)));
            assertThat(exception).hasMessage("Ingredient name 'Tomato' already used by an existing ingredient");
        }

        @Test
        @DisplayName("delete tomato successfully")
        void deleteTomato() {
            assumeThat(service.getIngredient(ID)).isNotNull();
            service.deleteIngredient(deleteCommand());
            assertThrows(EntityNotFoundException.class, () -> service.getIngredient(ID));
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
                cucumber = ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_QUANTITY_TYPES);
                ingredientRepository.save(cucumber);
            }

            @Test
            @DisplayName("return list containing all ingredients")
            void returnIngredientListWithAllIngredients() {
                assertThat(service.getIngredients()).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomato, cucumber);
            }

            @Test
            @DisplayName("return empty list after deleting all ingredients")
            void returnEmptyListAfterDeletion() {
                service.deleteIngredients();
                assertThat(service.getIngredients()).isEmpty();
            }

        }

    }

}
