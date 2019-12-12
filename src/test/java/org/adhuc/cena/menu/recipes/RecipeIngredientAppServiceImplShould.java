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

import static org.adhuc.cena.menu.ingredients.IngredientMother.ingredient;
import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
        var ingredientToRecipeAdditionService = new IngredientToRecipeAdditionService(recipeRepository, ingredientRepository);
        service = new RecipeIngredientAppServiceImpl(ingredientToRecipeAdditionService);
    }

    @Test
    @DisplayName("throw IllegalArgumentException when adding ingredient to recipe from null command")
    void throwIAEAddIngredientToRecipeNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> service.addIngredientToRecipe(null));
    }

    @Test
    @DisplayName("throw EntityNotFoundException when adding unknown ingredient to recipe")
    void addUnknownIngredientToRecipe() {
        recipeRepository.save(fromDefault().build());
        assumeThat(recipeRepository.exists(ID)).isTrue();
        assumeThat(ingredientRepository.exists(IngredientMother.ID)).isFalse();

        assertThrows(EntityNotFoundException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @DisplayName("throw EntityNotFoundException when adding ingredient to unknown recipe")
    void addIngredientToUnknownRecipe() {
        assumeThat(recipeRepository.exists(ID)).isFalse();
        ingredientRepository.save(ingredient());
        assumeThat(ingredientRepository.exists(IngredientMother.ID)).isTrue();

        assertThrows(EntityNotFoundException.class, () -> service.addIngredientToRecipe(addIngredientCommand()));
    }

    @Test
    @DisplayName("add ingredient to recipe successfully")
    void addIngredientToRecipe() {
        recipeRepository.save(fromDefault().build());
        assumeThat(recipeRepository.exists(ID)).isTrue();
        assumeThat(recipeRepository.findNotNullById(ID).ingredients()).isEmpty();
        ingredientRepository.save(ingredient());
        assumeThat(ingredientRepository.exists(IngredientMother.ID)).isTrue();

        service.addIngredientToRecipe(addIngredientCommand(IngredientMother.ID));

        assertThat(recipeRepository.findNotNullById(ID).ingredients()).contains(recipeIngredient());
    }

}
