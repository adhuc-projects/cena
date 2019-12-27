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

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * The {@link RecipeAuthor} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe author should")
class RecipeAuthorShould {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("not be creatable from invalid value")
    void notBeCreatableFromInvalidValue(String authorName) {
        assertThrows(IllegalArgumentException.class, () -> new RecipeAuthor(authorName));
    }

    @Test
    @DisplayName("contain author name used during creation")
    void containCreationValues() {
        var author = new RecipeAuthor("some author");
        assertThat(author.authorName()).isEqualTo("some author");
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when author name is equal")
    void isEqual(RecipeAuthor a1, RecipeAuthor a2, boolean expected) {
        assertThat(a1.equals(a2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when author name is equal")
    void sameHashCode(RecipeAuthor a1, RecipeAuthor a2, boolean expected) {
        assertThat(a1.hashCode() == a2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var recipeAuthor = new RecipeAuthor("some author");
        return Stream.of(
                Arguments.of(recipeAuthor, recipeAuthor, true),
                Arguments.of(recipeAuthor, new RecipeAuthor("some author"), true),
                Arguments.of(recipeAuthor, new RecipeAuthor("other author"), false)
        );
    }

    @Test
    @DisplayName("return author name during toString")
    void returnAuthorNameOnToString() {
        assertThat(new RecipeAuthor("some author").toString()).isEqualTo("some author");
    }

}
