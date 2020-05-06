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

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.*;

/**
 * The {@link RecipeIngredientRelatedService} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe ingredient related service should")
public class RecipeIngredientRelatedServiceShould {

    private RecipeRepository recipeRepository;
    private RecipeIngredientRelatedService service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        service = new RecipeIngredientRelatedService(recipeRepository);
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
