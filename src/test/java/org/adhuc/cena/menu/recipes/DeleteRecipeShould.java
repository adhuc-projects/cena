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
import org.junit.jupiter.params.provider.NullSource;

/**
 * The {@link DeleteRecipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe deletion command should")
class DeleteRecipeShould {

    @ParameterizedTest
    @NullSource
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(RecipeId recipeId) {
        assertThrows(IllegalArgumentException.class, () -> new DeleteRecipe(recipeId));
    }

    @Test
    @DisplayName("contain id used during creation")
    void containCreationValues() {
        var command = deleteCommand();
        assertThat(command.recipeId()).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when ingredient id is equal")
    void isEqual(DeleteRecipe c1, DeleteRecipe c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id is equal")
    void sameHashCode(DeleteRecipe c1, DeleteRecipe c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var deleteTomatoCucumberAndMozzaSalad = deleteCommand(TOMATO_CUCUMBER_MOZZA_SALAD_ID);
        return Stream.of(
                Arguments.of(deleteTomatoCucumberAndMozzaSalad, deleteTomatoCucumberAndMozzaSalad, true),
                Arguments.of(deleteTomatoCucumberAndMozzaSalad, deleteCommand(TOMATO_CUCUMBER_MOZZA_SALAD_ID), true),
                Arguments.of(deleteTomatoCucumberAndMozzaSalad, deleteCommand(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID), false)
        );
    }

}
