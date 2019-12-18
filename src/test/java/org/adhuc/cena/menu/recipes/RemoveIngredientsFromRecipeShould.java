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
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link RemoveIngredientsFromRecipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredients from recipe removal command should")
class RemoveIngredientsFromRecipeShould {

    @Test
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithNullRecipeId() {
        assertThrows(IllegalArgumentException.class, () -> new RemoveIngredientsFromRecipe(null));
    }

    @Test
    @DisplayName("contain recipe id used during creation")
    void containCreationValues() {
        var command = removeIngredientsCommand();
        assertThat(command.recipeId()).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when recipe id is equal")
    void isEqual(RemoveIngredientsFromRecipe c1, RemoveIngredientsFromRecipe c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when recipe id is equal")
    void sameHashCode(RemoveIngredientsFromRecipe c1, RemoveIngredientsFromRecipe c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var removeIngredients = new RemoveIngredientsFromRecipe(ID);
        return Stream.of(
                Arguments.of(removeIngredients, removeIngredients, true),
                Arguments.of(removeIngredients, new RemoveIngredientsFromRecipe(ID), true),
                Arguments.of(removeIngredients, new RemoveIngredientsFromRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID), false)
        );
    }

}
