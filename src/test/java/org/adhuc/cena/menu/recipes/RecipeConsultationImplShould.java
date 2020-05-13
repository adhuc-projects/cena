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
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.recipes.QueryRecipes.query;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.common.aggregate.Name;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientConsultation;
import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * The {@link RecipeConsultationImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe consultation service should")
class RecipeConsultationImplShould {

    private static final IngredientId MOZZA_ID = IngredientId.generate();
    private static final IngredientId FETA_ID = IngredientId.generate();

    private RecipeRepository recipeRepository;
    private IngredientConsultation ingredientConsultationMock;
    private RecipeConsultationImpl service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        ingredientConsultationMock = mock(IngredientConsultation.class);
        service = new RecipeConsultationImpl(recipeRepository, ingredientConsultationMock);

        when(ingredientConsultationMock.getIngredient(TOMATO_ID)).thenReturn(ingredient(TOMATO_ID, TOMATO, TOMATO_MEASUREMENT_TYPES));
        when(ingredientConsultationMock.getIngredient(CUCUMBER_ID)).thenReturn(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));
        when(ingredientConsultationMock.getIngredient(MOZZA_ID)).thenReturn(ingredient(MOZZA_ID, new Name("Mozzarella"), MEASUREMENT_TYPES));
        when(ingredientConsultationMock.getIngredient(FETA_ID)).thenReturn(ingredient(FETA_ID, new Name("Feta"), MEASUREMENT_TYPES));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting recipes from null query")
    void throwIAEGetRecipesNullQuery() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipes(null));
    }

    @Test
    @DisplayName("return unmodifiable list of recipes")
    void returnUnmodifiableListOfRecipes() {
        assertThrows(UnsupportedOperationException.class, () -> service.getRecipes(query()).add(recipe()));
    }

    @Test
    @DisplayName("throw EntityNotFoundException when filtering on unknown ingredient identity")
    void throwEntityNotFoundExceptionRecipeListFilteredOnUnknownIngredientIdentity() {
        var ingredientId = IngredientId.generate();
        when(ingredientConsultationMock.getIngredient(ingredientId)).thenThrow(new EntityNotFoundException(Ingredient.class, ingredientId));

        assertThrows(EntityNotFoundException.class, () -> service.getRecipes(query().withIngredientId(ingredientId)));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when checking if recipe exists from null identity")
    void throwIAEExistsNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.exists(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when getting recipe from null identity")
    void throwIAEGetRecipeNullId() {
        assertThrows(IllegalArgumentException.class, () -> service.getRecipe(null));
    }

    @Test
    @DisplayName("return false when indicating if ingredients are related with no recipe")
    void ingredientsNotRelatedWhenNoRecipe() {
        assertThat(service.areIngredientsRelated()).isFalse();
    }

    @Test
    @DisplayName("return false when indicating if tomato is related with no recipe")
    void tomatoNotRelatedWhenNoRecipe() {
        assertThat(service.isIngredientRelated(TOMATO_ID)).isFalse();
    }

    @Nested
    @DisplayName("with no recipe")
    class WithNoRecipe {

        @Test
        @DisplayName("return empty list of recipes when no filtering applies on ingredients")
        void returnEmptyRecipeList() {
            assertThat(service.getRecipes(query())).isEmpty();
        }

        @Test
        @DisplayName("return empty list of recipes when filtering on mozzarella")
        void returnEmptyRecipeListFilteredOnMozza() {
            assertThat(service.getRecipes(query().withIngredientId(MOZZA_ID))).isEmpty();
        }

        @Test
        @DisplayName("indicate that recipe does not exist")
        void indicateRecipeDoesNotExist() {
            assertThat(service.exists(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isFalse();
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
            recipeRepository.save(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return list of recipes containing tomato, cucumber and mozzarella salad when no filtering applies on ingredients")
        void returnRecipeListWithTomatoCucumberAndMozzaSalad() {
            assertThat(service.getRecipes(query())).isNotEmpty().usingFieldByFieldElementComparator()
                    .containsExactly(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return list of recipes containing tomato, cucumber and mozzarella salad when filtering on mozzarella")
        void returnRecipeListWithTomatoCucumberAndMozzaSaladFilteredOnMozza() {
            assertThat(service.getRecipes(query().withIngredientId(MOZZA_ID))).isNotEmpty().usingFieldByFieldElementComparator()
                    .containsExactly(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("return empty list of recipes when filtering on feta")
        void returnEmptyRecipeListFilteredOnFeta() {
            assertThat(service.getRecipes(query().withIngredientId(FETA_ID))).isEmpty();
        }

        @Test
        @DisplayName("throw EntityNotFoundException when getting recipe from unknown identity")
        void throwEntityNotFoundExceptionUnknownId() {
            assertThrows(EntityNotFoundException.class, () -> service.getRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID));
        }

        @Test
        @DisplayName("indicate that recipe with unknown identity does not exist")
        void indicateRecipeWithUnknownIdDoesNotExist() {
            assertThat(service.exists(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)).isFalse();
        }

        @Test
        @DisplayName("return recipe when getting recipe from tomato, cucumber and mozzarella salad id")
        void returnTomatoCucumberAndMozzaSalad() {
            assertThat(service.getRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isEqualToComparingFieldByField(tomatoCucumberAndMozzaSalad);
        }

        @Test
        @DisplayName("indicate that tomato, cucumber and mozzarella salad exists")
        void indicateTomatoCucumberAndMozzaSaladExists() {
            assertThat(service.exists(TOMATO_CUCUMBER_MOZZA_SALAD_ID)).isTrue();
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
                recipeRepository.save(tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return list containing all recipes")
            void returnRecipeListWithAllRecipes() {
                assertThat(service.getRecipes(query())).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomatoCucumberAndMozzaSalad, tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return list containing all recipes when filtering on tomato")
            void returnRecipeListWithAllRecipesFilteredOnTomato() {
                assertThat(service.getRecipes(query().withIngredientId(TOMATO_ID))).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomatoCucumberAndMozzaSalad, tomatoCucumberOliveAndFetaSalad);
            }

            @Test
            @DisplayName("return list of recipes containing tomato, cucumber and mozzarella salad when filtering on mozzarella")
            void returnRecipeListWithTomatoCucumberAndMozzaSaladFilteredOnMozza() {
                assertThat(service.getRecipes(query().withIngredientId(MOZZA_ID))).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactly(tomatoCucumberAndMozzaSalad);
            }

            @Test
            @DisplayName("return list of recipes containing tomato, cucumber, olive and feta when filtering on feta")
            void returnEmptyRecipeListFilteredOnFeta() {
                assertThat(service.getRecipes(query().withIngredientId(FETA_ID))).isNotEmpty().usingFieldByFieldElementComparator()
                        .containsExactlyInAnyOrder(tomatoCucumberOliveAndFetaSalad);
            }

        }

    }

    @Nested
    @DisplayName("with both known ingredient and recipe")
    class WithIngredientAndRecipe {
        @BeforeEach
        void setUp() {
            recipeRepository.save(builder().withIngredient(CUCUMBER_ID, false).build());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
            assumeThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients())
                    .containsExactly(recipeIngredient(CUCUMBER_ID, false, QUANTITY));
        }

        @Test
        @DisplayName("return true when indicating if ingredients are related with one recipe related to ingredient")
        void ingredientsRelatedWhenOneRecipeRelatedToIngredient() {
            assertThat(service.areIngredientsRelated()).isTrue();
        }

        @Test
        @DisplayName("return false when indicating if tomato is related with no recipe related to tomato")
        void tomatoNotRelated() {
            assertThat(service.isIngredientRelated(TOMATO_ID)).isFalse();
        }

        @Test
        @DisplayName("return true when indicating if cucumber is related with one recipe related to cucumber")
        void cucumberRelated() {
            assertThat(service.isIngredientRelated(CUCUMBER_ID)).isTrue();
        }

    }

}
