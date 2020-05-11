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
import static org.adhuc.cena.menu.recipes.MeasurementUnit.CENTILITER;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.aggregate.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientConsultationAppService;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * The {@link RecipeIngredientAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe ingredient service should")
class RecipeIngredientAppServiceImplShould {

    private RecipeRepository recipeRepository;
    private IngredientConsultationAppService ingredientAppServiceMock;
    private RecipeIngredientAppServiceImpl service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        ingredientAppServiceMock = mock(IngredientConsultationAppService.class);
        var additionService = new IngredientToRecipeAdditionService(recipeRepository, ingredientAppServiceMock);
        var removalService = new IngredientFromRecipeRemovalService(recipeRepository, ingredientAppServiceMock);
        service = new RecipeIngredientAppServiceImpl(additionService, removalService, recipeRepository);
    }

    @Test
    @DisplayName("throw IllegalArgumentException when adding ingredient to recipe from null command")
    void throwIAEAddIngredientToRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.addIngredientToRecipe(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredient from recipe from null command")
    void throwIAERemoveIngredientFromRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.removeIngredientFromRecipe(null));
    }

    @Test
    @DisplayName("throw IllegalArgumentException when removing ingredients from recipe from null command")
    void throwIAERemoveIngredientsFromRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.removeIngredientsFromRecipe(null));
    }

    @Nested
    @DisplayName("with unknown ingredient")
    class WithUnknownIngredient {
        @BeforeEach
        void setUp() {
            recipeRepository.save(builder().build());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
            when(ingredientAppServiceMock.getIngredient(IngredientMother.ID))
                    .thenThrow(new EntityNotFoundException(Ingredient.class, IngredientMother.ID));
        }

        @Test
        @DisplayName("throw EntityNotFoundException when adding unknown ingredient to recipe")
        void addUnknownIngredientToRecipe() {
            assertThrows(EntityNotFoundException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
        }

        @Test
        @DisplayName("throw EntityNotFoundException when removing unknown ingredient from recipe")
        void removeUnknownIngredientFromRecipe() {
            assertThrows(EntityNotFoundException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand()));
        }
    }

    @Nested
    @DisplayName("with unknown recipe")
    class WithUnknownRecipe {
        @BeforeEach
        void setUp() {
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isFalse();
            when(ingredientAppServiceMock.getIngredient(IngredientMother.ID)).thenReturn(ingredient());
        }

        @Test
        @DisplayName("throw EntityNotFoundException when adding ingredient to unknown recipe")
        void addIngredientToUnknownRecipe() {
            assertThrows(EntityNotFoundException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
        }

        @Test
        @DisplayName("throw EntityNotFoundException when removing ingredient from unknown recipe")
        void removeIngredientFromUnknownRecipe() {
            assertThrows(EntityNotFoundException.class, () -> service.removeIngredientFromRecipe(removeIngredientCommand()));
        }

        @Test
        @DisplayName("throw EntityNotFoundException when removing ingredients from unknown recipe")
        void removeIngredientsFromUnknownRecipe() {
            assertThrows(EntityNotFoundException.class, () -> service.removeIngredientsFromRecipe(removeIngredientsCommand()));
        }
    }

    @Nested
    @DisplayName("with both known ingredient and recipe")
    class WithIngredientAndRecipe {
        @BeforeEach
        void setUp() {
            when(ingredientAppServiceMock.getIngredient(IngredientMother.ID)).thenReturn(ingredient());
            when(ingredientAppServiceMock.getIngredient(CUCUMBER_ID)).thenReturn(ingredient(CUCUMBER_ID, CUCUMBER, CUCUMBER_MEASUREMENT_TYPES));

            recipeRepository.save(builder().withIngredient(CUCUMBER_ID, false).build());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
            assumeThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients())
                    .containsExactly(recipeIngredient(CUCUMBER_ID, false, QUANTITY));
        }

        @Test
        @DisplayName("add ingredient to recipe with measurement unit not corresponding to ingredient's measurement type")
        void addIngredientToRecipeMeasurementUnitNotCorrespondingToMeasurementType() {
            var command = addIngredientCommand(IngredientMother.ID, RecipeMother.ID, MAIN_INGREDIENT, new Quantity(200, CENTILITER));
            var exception = assertThrows(InvalidMeasurementUnitForIngredientException.class,
                    () -> service.addIngredientToRecipe(command));
            assertThat(exception.getMessage()).isEqualTo("Unable to add ingredient '" + IngredientMother.ID +
                    "' to recipe '" + RecipeMother.ID + "': measurement unit " + CENTILITER +
                    " does not correspond to ingredient's measurement types " + MEASUREMENT_TYPES);
        }

        @Test
        @DisplayName("add ingredient to recipe successfully")
        void addIngredientToRecipe() {
            service.addIngredientToRecipe(addIngredientCommand(IngredientMother.ID, MAIN_INGREDIENT));
            assertThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients()).contains(recipeIngredient());
        }

        @Test
        @DisplayName("remove ingredient from recipe successfully")
        void removeIngredientFromRecipe() {
            service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
            assertThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients())
                    .doesNotContain(recipeIngredient(CUCUMBER_ID, MAIN_INGREDIENT, QUANTITY));
        }

        @Nested
        @DisplayName("with recipe related to multiple ingredients")
        class WithRecipeWithMultipleIngredients {
            @BeforeEach
            void setUp() {
                recipeRepository.save(recipe());
                assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
                assumeThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients())
                        .hasSizeGreaterThanOrEqualTo(2);
            }

            @Test
            @DisplayName("remove ingredients from recipe successfully")
            void removeIngredientsFromRecipe() {
                service.removeIngredientsFromRecipe(removeIngredientsCommand());
                assertThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients()).isEmpty();
            }
        }

    }

}
