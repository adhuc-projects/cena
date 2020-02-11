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

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.ID;
import static org.adhuc.cena.menu.recipes.QueryRecipes.query;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link QueryRecipes} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipes query should")
class QueryRecipesShould {

    @Test
    @DisplayName("contain no filter by default")
    void containNoFilterByDefault() {
        var query = query();
        assertThat(query.ingredientId()).isEmpty();
    }

    @Test
    @DisplayName("not accept null ingredient")
    void notAcceptNullIngredient() {
        assertThrows(IllegalArgumentException.class, () -> query().withIngredientId(null));
    }

    @Test
    @DisplayName("contain ingredient when specified")
    void containIngredientWhenSpecified() {
        var query = query().withIngredientId(ID);
        assertThat(query.ingredientId()).isPresent().contains(ID);
    }

    @Test
    @DisplayName("create new reference when specifying ingredient")
    void newReferenceWithIngredient() {
        var original = query();
        var withIngredient = original.withIngredientId(ID);
        assertThat(original == withIngredient).isFalse();
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when ingredient id are equal")
    void isEqual(QueryRecipes q1, QueryRecipes q2, boolean expected) {
        assertThat(q1.equals(q2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id are equal")
    void sameHashCode(QueryRecipes q1, QueryRecipes q2, boolean expected) {
        assertThat(q1.hashCode() == q2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var emptyQuery = query();
        var query = emptyQuery.withIngredientId(ID);
        return Stream.of(
                Arguments.of(emptyQuery, emptyQuery, true),
                Arguments.of(query, query, true),
                Arguments.of(emptyQuery, query(), true),
                Arguments.of(query, query().withIngredientId(ID), true),
                Arguments.of(emptyQuery, query, false),
                Arguments.of(query, query().withIngredientId(CUCUMBER_ID), false)
        );
    }

}
