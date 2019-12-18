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

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.IngredientMother;
import org.adhuc.cena.menu.ingredients.IngredientRepository;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryIngredientRepository;
import org.adhuc.cena.menu.port.adapter.persistence.memory.InMemoryRecipeRepository;

/**
 * The {@link RecipeIngredientAppServiceImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe ingredient service should")
class RecipeIngredientAppServiceImplShould {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    private RecipeIngredientAppServiceImpl service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        ingredientRepository = new InMemoryIngredientRepository();
        var additionService = new IngredientToRecipeAdditionService(recipeRepository, ingredientRepository);
        var removalService = new IngredientFromRecipeRemovalService(recipeRepository, ingredientRepository);
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
            recipeRepository.save(fromDefault().build());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
            assumeThat(ingredientRepository.exists(IngredientMother.ID)).isFalse();
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
            ingredientRepository.save(ingredient());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isFalse();
            assumeThat(ingredientRepository.exists(IngredientMother.ID)).isTrue();
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
            recipeRepository.save(fromDefault().withIngredients(CUCUMBER_ID).build());
            assumeThat(recipeRepository.exists(RecipeMother.ID)).isTrue();
            assumeThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients())
                    .containsExactly(recipeIngredient(CUCUMBER_ID));
            ingredientRepository.save(ingredient());
            ingredientRepository.save(ingredient(CUCUMBER_ID, CUCUMBER));
            assumeThat(ingredientRepository.exists(IngredientMother.ID)).isTrue();
            assumeThat(ingredientRepository.exists(CUCUMBER_ID)).isTrue();
        }

        @Test
        @DisplayName("add ingredient to recipe successfully")
        void addIngredientToRecipe() {
            service.addIngredientToRecipe(addIngredientCommand(IngredientMother.ID));
            assertThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients()).contains(recipeIngredient());
        }

        @Test
        @DisplayName("remove ingredient from recipe successfully")
        void removeIngredientFromRecipe() {
            service.removeIngredientFromRecipe(removeIngredientCommand(CUCUMBER_ID));
            assertThat(recipeRepository.findNotNullById(RecipeMother.ID).ingredients()).doesNotContain(recipeIngredient(CUCUMBER_ID));
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
