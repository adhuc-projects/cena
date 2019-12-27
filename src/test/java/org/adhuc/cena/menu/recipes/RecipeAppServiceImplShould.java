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
package org.adhuc.cena.menu.recipes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryRecipeRepository;

/**
 * The {@link RecipeAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe service should")
class RecipeAppServiceImplShould {

    private RecipeRepository recipeRepository;
    private RecipeAppServiceImpl service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        service = new RecipeAppServiceImpl(recipeRepository);
    }

    @Test
    @DisplayName("return unmodifiable list of recipes")
    void returnUnmodifiableListOfRecipes() {
        assertThrows(UnsupportedOperationException.class, () -> service.getRecipes().add(recipe()));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting recipe from null identity")
    void throwIAEGetRecipeNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipe(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when creating recipe from null command")
    void throwIAECreateRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.createRecipe(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when deleting recipe from null command")
    void throwIAEDeleteRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteRecipe(null));
    }

    @Test
    @DisplayName("create recipe successfully")
    void createRecipe() {
        var recipe = builder().build();
        var created = service.createRecipe(createCommand(recipe));
        assertThat(created).isNotNull().isEqualToComparingFieldByField(recipe);
    }

    @Test
    @DisplayName("retrieve recipe with identity after creation")
    void retrieveRecipeWithIdAfterCreation() {
        var recipe = builder().build();
        service.createRecipe(createCommand(recipe));
        assertThat(service.getRecipe(recipe.id())).isNotNull().isEqualToComparingFieldByField(recipe);
    }

    @Nested
    @DisplayName("with no recipe")
    class WithNoRecipe {

        @Test
        @DisplayName("return empty list of recipes")
        void returnEmptyRecipeList() {
            assertThat(service.getRecipes()).isEmpty();
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
                    .build();
            recipeRepository.save(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return list of recipes containing tomato, cucumber and mozzarella salad")
        void returnRecipeListWithTomatoCucumberAndMozzaSalad() {
            assertThat(service.getRecipes()).isNotEmpty().usingFieldByFieldElementComparator()
                    .containsExactly(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("throw EntityNotFoundException when getting recipe from unknown identity")
        void throwEntityNotFoundExceptionUnknownId() {
            assertThrows(EntityNotFoundException.class, () -> service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        }

        @Test
        @DisplayName("return recipe when getting recipe from tomato, cucumber and mozzarella salad id")
        void returnTomatoCucumberAndMozzaSalad() {
            assertThat(service.getRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isEqualToComparingFieldByField(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("delete tomato, cucumber and mozzarella salad successfully")
        void deleteTomatoCucumberAndMozzaSalad() {
            assumeThat(service.getRecipe(ID)).isNotNull();
            service.deleteRecipe(deleteCommand());
            assertThrows(EntityNotFoundException.class, () -> service.getRecipe(ID));
        }

        @Test
        @DisplayName("throw EntityNotFoundException when deleting unknown recipe")
        void throwEntityNotFoundDeleteUnknownRecipe() {
            var exception = assertThrows(EntityNotFoundException.class,
                    () -> service.deleteRecipe(deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)));
            assertThat(exception.getIdentity()).isEqualTo(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID.toString());
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
                        .build();
                recipeRepository.save(tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return list containing all recipes")
            void returnRecipeListWithAllRecipes() {
                assertThat(service.getRecipes()).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomatoCucumberAndMozzaSalad, tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return empty list after deleting all recipes")
            void returnEmptyListAfterDeletion() {
                service.deleteRecipes();
                assertThat(service.getRecipes()).isEmpty();
            }

        }

    }

}
