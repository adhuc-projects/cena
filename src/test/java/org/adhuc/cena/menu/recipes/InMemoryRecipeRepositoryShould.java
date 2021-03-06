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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * The {@link InMemoryRecipeRepository} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("inMemoryRepository")
@DisplayName("In-memory recipe repository should")
class InMemoryRecipeRepositoryShould {

    private static final IngredientId MOZZA_ID = IngredientId.generate();
    private static final IngredientId FETA_ID = IngredientId.generate();

    private InMemoryRecipeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRecipeRepository();
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting recipe from null identity")
    void throwIAEFindByIdNullId() {
        assertThrows(IllegalArgumentException.class, () -> repository.findById(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting not null recipe from null identity")
    void throwIAEFindNotNullByIdNullId() {
        assertThrows(IllegalArgumentException.class, () -> repository.findNotNullById(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when saving null recipe")
    void throwIAESavingNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when deleting null recipe")
    void throwIAEDeletingNullRecipe() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
    }

    @Nested
    @DisplayName("with no recipe")
    class WithNoEntity {

        @Test
        @DisplayName("return empty collection when finding all recipes")
        void returnEmptyCollection() {
            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("return empty collection when finding recipes composed of ingredient")
        void returnEmptyCollectionComposedOfIngredient() {
            assertThat(repository.findByIngredient(IngredientMother.ID)).isEmpty();
        }

    }

    @Nested
    @DisplayName("with tomato, cucumber and mozzarella salad")
    class WithTomatoCucumberAndMozzaSalad {

        private Recipe tomatoCucumberAndMozzaSalad;

        @BeforeEach
        void setUp() {
            tomatoCucumberAndMozzaSalad = builder()
                    .withId(TOMATO_CUCUMBER_MOZZA_SALAD_ID)
                    .withName(TOMATO_CUCUMBER_MOZZA_SALAD_NAME)
                    .withContent(TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT)
                    .withAuthor(TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR)
                    .withIngredients(TOMATO_ID, CUCUMBER_ID, MOZZA_ID)
                    .build();
            repository.save(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return a collection containing recipe")
        void returnCollectionContainingRecipe() {
            assertThat(repository.findAll()).usingFieldByFieldElementComparator().containsExactly(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return a collection containing recipe when finding recipes composed of mozzarella")
        void returnCollectionContainingRecipeComposedOfMozza() {
            assertThat(repository.findByIngredient(MOZZA_ID)).usingFieldByFieldElementComparator().containsExactly(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return a collection containing recipe when finding recipes composed of feta")
        void returnEmptyCollectionComposedOfFeta() {
            assertThat(repository.findByIngredient(FETA_ID)).isEmpty();
        }

        @Test
        @DisplayName("indicate that tomato, cucumber and mozzarella salad exists")
        void exists() {
            assertThat(repository.exists(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isTrue();
        }

        @Test
        @DisplayName("indicate that tomato, cucumber, olives and feta salad does not exist")
        void doesntExist() {
            assertThat(repository.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isFalse();
        }

        @Test
        @DisplayName("return empty recipe when getting recipe with unknown id")
        void returnEmptyUnknownId() {
            assertThat(repository.findById(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isEmpty();
        }

        @Test
        @DisplayName("throw EntityNotFoundException when getting not null recipe with unknown id")
        void throwEntityNotFoundExceptionUnknownId() {
            assertThrows(EntityNotFoundException.class, () -> repository.findNotNullById(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        }

        @Test
        @DisplayName("return tomato, cucumber and mozzarella salad when getting recipe with recipe id")
        void returnRecipe() {
            assertThat(repository.findById(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isPresent().contains(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return tomato, cucumber and mozzarella salad when getting not null recipe with recipe id")
        void returnRecipeNotNull() {
            assertThat(repository.findNotNullById(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isEqualToComparingFieldByField(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("delete tomato, cucumber and mozzarella salad successfully")
        void deleteRecipe() {
            repository.delete(tomatoCucumberAndMozzaSalad);
            assertThat(repository.findAll()).doesNotContain(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("delete recipe with tomato, cucumber and mozzarella salad identity and other name and content")
        void deleteWithIdAndDifferentNameAndContent() {
            var recipe = builder()
                    .withId(TOMATO_CUCUMBER_MOZZA_SALAD_ID)
                    .withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME)
                    .withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT)
                    .withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR)
                    .withIngredients(TOMATO_ID, CUCUMBER_ID)
                    .build();
            repository.delete(recipe);
            assertThat(repository.findAll()).doesNotContain(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("do nothing when deleting unknown recipe")
        void deleteUnknownRecipe() {
            var recipe = builder()
                    .withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)
                    .withName(TOMATO_CUCUMBER_MOZZA_SALAD_NAME)
                    .withContent(TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT)
                    .withAuthor(TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR)
                    .build();
            repository.delete(recipe);
            assertThat(repository.findAll()).containsOnly(tomatoCucumberAndMozzaSalad);
        }

        @Nested
        @DisplayName("and tomato, cucumber, olive and feta salad")
        class AndTomatoCucumberOliveAndFetaSalad {

            private Recipe tomatoCucumberOliveAndFetaSalad;

            @BeforeEach
            void setUp() {
                tomatoCucumberOliveAndFetaSalad = builder()
                        .withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)
                        .withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME)
                        .withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT)
                        .withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR)
                        .withIngredients(TOMATO_ID, CUCUMBER_ID, FETA_ID)
                        .build();
                repository.save(tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("returns a collection containing both recipes")
            void returnCollectionContainingRecipes() {
                assertThat(repository.findAll()).containsExactlyInAnyOrder(tomatoCucumberAndMozzaSalad, tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("returns a collection containing both recipes when finding recipes composed of tomato")
            void returnCollectionContainingRecipesComposedOfTomato() {
                assertThat(repository.findByIngredient(TOMATO_ID)).containsExactlyInAnyOrder(tomatoCucumberAndMozzaSalad, tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return a collection containing only one recipe when finding recipes composed of mozzarella")
            void returnCollectionContainingRecipeComposedOfMozza() {
                assertThat(repository.findByIngredient(MOZZA_ID)).usingFieldByFieldElementComparator().containsExactly(tomatoCucumberAndMozzaSalad);
            }

            @Test
            @DisplayName("return a collection containing only one recipe when finding recipes composed of feta")
            void returnEmptyCollectionComposedOfFeta() {
                assertThat(repository.findByIngredient(FETA_ID)).usingFieldByFieldElementComparator().containsExactly(tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return empty collection after deleting all recipes")
            void returnEmptyCollectionAfterDeletion() {
                repository.deleteAll();
                assertThat(repository.findAll()).isEmpty();
            }

        }

    }

}
