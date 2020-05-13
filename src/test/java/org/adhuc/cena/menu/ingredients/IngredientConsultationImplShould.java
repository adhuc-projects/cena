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

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;

/**
 * The {@link IngredientConsultationImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Ingredient consultation service should")
@ExtendWith(MockitoExtension.class)
class IngredientConsultationImplShould {

    private IngredientRepository ingredientRepository;
    private IngredientConsultationImpl service;

    @BeforeEach
    void setUp() {
        ingredientRepository = new InMemoryIngredientRepository();
        service = new IngredientConsultationImpl(ingredientRepository);
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

        }

    }

}
