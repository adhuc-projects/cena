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

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * The {@link RecipeAdministrationImpl} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("appService")
@DisplayName("Recipe ingredient service should")
class RecipeAdministrationImplShould {

    private static final IngredientId MOZZA_ID = IngredientId.generate();
    private static final IngredientId FETA_ID = IngredientId.generate();

    private RecipeRepository recipeRepository;
    private RecipeAdministrationImpl service;

    @BeforeEach
    void setUp() {
        recipeRepository = new InMemoryRecipeRepository();
        service = new RecipeAdministrationImpl(recipeRepository);

        var tomatoCucumberAndMozzaSalad = builder()
                .withId(TOMATO_CUCUMBER_MOZZA_SALAD_ID)
                .withName(TOMATO_CUCUMBER_MOZZA_SALAD_NAME)
                .withContent(TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT)
                .withAuthor(TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR)
                .withIngredients(TOMATO_ID, CUCUMBER_ID, MOZZA_ID)
                .build();
        recipeRepository.save(tomatoCucumberAndMozzaSalad);
        var tomatoCucumberOliveAndFetaSalad = builder()
                .withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)
                .withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME)
                .withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT)
                .withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR)
                .withIngredients(TOMATO_ID, CUCUMBER_ID, FETA_ID)
                .build();
        recipeRepository.save(tomatoCucumberOliveAndFetaSalad);
    }

    @Test
    @DisplayName("return empty list after deleting all recipes")
    void returnEmptyListAfterDeletion() {
        service.deleteRecipes();
        assertThat(recipeRepository.findAll()).isEmpty();
    }

}
