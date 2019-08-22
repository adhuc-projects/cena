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
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryIngredientRepository;

/**
 * The {@link IngredientAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient service should")
class IngredientAppServiceImplShould {

    private IngredientAppServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IngredientAppServiceImpl(new InMemoryIngredientRepository());
    }

    @Test
    @DisplayName("return unmodifiable list of ingredients")
    void returnUnmodifiableListOfIngredients() {
        assertThrows(UnsupportedOperationException.class, () -> service.getIngredients().add(ingredient()));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when creating ingredient from null command")
    void throwIAECreateIngredientNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.createIngredient(null));
    }

    @Nested
    @DisplayName("with no ingredient")
    class WithNoIngredient {

        @Test
        @DisplayName("return empty list of ingredients")
        void returnEmptyIngredientList() {
            assertThat(service.getIngredients()).isEmpty();
        }

    }

    @Nested
    @DisplayName("with tomato")
    class WithTomato {

        private Ingredient tomato;

        @BeforeEach
        void setUp() {
            tomato = ingredient(TOMATO);
            service.createIngredient(createCommand(tomato));
        }

        @Test
        @DisplayName("return list of ingredients containing tomato")
        void returnIngredientListWithTomato() {
            assertThat(service.getIngredients()).isNotEmpty().usingFieldByFieldElementComparator()
                    .containsExactly(tomato);
        }

        @Nested
        @DisplayName("and cucumber")
        class AndCucumber {

            private Ingredient cucumber;

            @BeforeEach
            void setUp() {
                cucumber = ingredient(CUCUMBER);
                service.createIngredient(createCommand(cucumber));
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
