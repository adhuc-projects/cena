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
package org.adhuc.cena.menu.port.adapter.persistence.memory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;

/**
 * The {@link InMemoryIngredientRepository} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("inMemoryRepository")
@DisplayName("In-memory ingredient repository should")
class InMemoryIngredientRepositoryShould {

    private InMemoryIngredientRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryIngredientRepository();
    }

    @Test
    @DisplayName("throw IllegalArgumentException when saving null ingredient")
    void throwIAESavingNullIngredient() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Nested
    @DisplayName("with no ingredient")
    class WithNoEntity {

        @Test
        @DisplayName("return empty collection")
        void returnEmptyCollection() {
            assertThat(repository.findAll()).isEmpty();
        }

    }

    @Nested
    @DisplayName("with tomato")
    class WithIngredient {

        private Ingredient tomato;

        @BeforeEach
        void setUp() {
            tomato = ingredient(TOMATO_ID, TOMATO);
            repository.save(tomato);
        }

        @Test
        @DisplayName("return a collection containing tomato")
        void returnCollectionContainingIngredient() {
            assertThat(repository.findAll()).containsExactly(tomato);
        }

        @Test
        @DisplayName("throw IllegalArgumentException when getting ingredient from null identity")
        void throwIAEFindByIdNullId() {
            assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
        }

        @Test
        @DisplayName("throw IllegalArgumentException when getting not null ingredient from null identity")
        void throwIAEFindNotNullByIdNullId() {
            assertThrows(IllegalArgumentException.class, () -> repository.findNotNullById(null));
        }

        @Test
        @DisplayName("return empty ingredient when getting ingredient with unknown id")
        void returnEmptyUnknownId() {
            assertThat(repository.findById(CUCUMBER_ID)).isNotPresent();
        }

        @Test
        @DisplayName("throw EntityNotFoundException when getting not null ingredient with unknown id")
        void throwEntityNotFoundExceptionUnknownId() {
            assertThrows(EntityNotFoundException.class, () -> repository.findNotNullById(CUCUMBER_ID));
        }

        @Test
        @DisplayName("return tomato when getting ingredient with tomato id")
        void returnTomato() {
            assertThat(repository.findById(TOMATO_ID)).isPresent().contains(tomato);
        }

        @Test
        @DisplayName("return tomato when getting not null ingredient with tomato id")
        void returnTomatoNotNull() {
            assertThat(repository.findNotNullById(TOMATO_ID)).isEqualToComparingFieldByField(tomato);
        }

        @Nested
        @DisplayName("and cucumber")
        class AndCucumber {

            private Ingredient cucumber;

            @BeforeEach
            void setUp() {
                cucumber = ingredient(CUCUMBER_ID, CUCUMBER);
                repository.save(cucumber);
            }

            @Test
            @DisplayName("returns a collection containing tomato and cucumber")
            void returnCollectionContainingIngredients() {
                assertThat(repository.findAll()).containsExactlyInAnyOrder(tomato, cucumber);
            }

            @Test
            @DisplayName("return empty collection after deleting all ingredients")
            void returnEmptyCollectionAfterDeletion() {
                repository.deleteAll();
                assertThat(repository.findAll()).isEmpty();
            }

        }

    }

}
