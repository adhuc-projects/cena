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
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.recipes.RecipeMother.*;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.adhuc.cena.menu.common.aggregate.Name;

/**
 * The {@link CreateRecipe} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Recipe creation command should")
class CreateRecipeShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(RecipeId recipeId, Name name, String content, RecipeAuthor author,
                                             Servings servings, Set<CourseType> courseTypes) {
        assertThrows(IllegalArgumentException.class, () -> new CreateRecipe(recipeId, name, content, author, servings, courseTypes));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME, CONTENT, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, null, CONTENT, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, null, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, "", AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, null, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, null, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, SERVINGS, null)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParametersWithoutServings")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParametersWithoutServings(RecipeId recipeId, Name name, String content,
                                                            RecipeAuthor author, Set<CourseType> courseTypes) {
        assertThrows(IllegalArgumentException.class, () -> new CreateRecipe(recipeId, name, content, author, courseTypes));
    }

    private static Stream<Arguments> invalidCreationParametersWithoutServings() {
        return Stream.of(
                Arguments.of(null, NAME, CONTENT, AUTHOR, COURSE_TYPES),
                Arguments.of(ID, null, CONTENT, AUTHOR, COURSE_TYPES),
                Arguments.of(ID, NAME, null, AUTHOR, COURSE_TYPES),
                Arguments.of(ID, NAME, "", AUTHOR, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, null, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, null)
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParameters")
    @DisplayName("contain values used during creation")
    void containCreationValues(RecipeId recipeId, Name name, String content, RecipeAuthor author,
                               Servings servings, Set<CourseType> courseTypes) {
        var command = new CreateRecipe(recipeId, name, content, author, servings, courseTypes);
        assertSoftly(softly -> {
            softly.assertThat(command.recipeId()).isEqualTo(recipeId);
            softly.assertThat(command.recipeName()).isEqualTo(name);
            softly.assertThat(command.recipeContent()).isEqualTo(content);
            softly.assertThat(command.recipeAuthor()).isEqualTo(author);
            softly.assertThat(command.servings()).isEqualTo(servings);
            softly.assertThat(command.courseTypes()).isEqualTo(courseTypes);
        });
    }

    private static Stream<Arguments> validCreationParameters() {
        return Stream.of(
                Arguments.of(ID, NAME, CONTENT, AUTHOR, SERVINGS, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, SERVINGS, Set.of())
        );
    }

    @ParameterizedTest
    @MethodSource("validCreationParametersWithoutServings")
    @DisplayName("contain values used during creation without servings")
    void containCreationValuesWithoutServings(RecipeId recipeId, Name name, String content, RecipeAuthor author,
                                              Set<CourseType> courseTypes) {
        var command = new CreateRecipe(recipeId, name, content, author, courseTypes);
        assertSoftly(softly -> {
            softly.assertThat(command.recipeId()).isEqualTo(recipeId);
            softly.assertThat(command.recipeName()).isEqualTo(name);
            softly.assertThat(command.recipeContent()).isEqualTo(content);
            softly.assertThat(command.recipeAuthor()).isEqualTo(author);
            softly.assertThat(command.servings()).isEqualTo(Servings.DEFAULT);
            softly.assertThat(command.courseTypes()).isEqualTo(courseTypes);
        });
    }

    private static Stream<Arguments> validCreationParametersWithoutServings() {
        return Stream.of(
                Arguments.of(ID, NAME, CONTENT, AUTHOR, COURSE_TYPES),
                Arguments.of(ID, NAME, CONTENT, AUTHOR, Set.of())
        );
    }

    @Test
    @DisplayName("return non modifiable course types set")
    void courseTypesNotModifiable() {
        var command = createCommand();
        assertThrows(UnsupportedOperationException.class, () -> command.courseTypes().add(CourseType.DESSERT));
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when recipe id, name and content are equal")
    void isEqual(CreateRecipe c1, CreateRecipe c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when recipe id, name and content are equal")
    void sameHashCode(CreateRecipe c1, CreateRecipe c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var createTomatoCucumberAndMozzaSalad = new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT, TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR,
                TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS, TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES);
        return Stream.of(
                Arguments.of(createTomatoCucumberAndMozzaSalad, createTomatoCucumberAndMozzaSalad, true),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), true),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), false),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), false),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), false),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), false),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_OLIVE_FETA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_MOZZA_SALAD_COURSE_TYPES), false),
                Arguments.of(createTomatoCucumberAndMozzaSalad, new CreateRecipe(TOMATO_CUCUMBER_MOZZA_SALAD_ID,
                        TOMATO_CUCUMBER_MOZZA_SALAD_NAME, TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT,
                        TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR, TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS,
                        TOMATO_CUCUMBER_OLIVE_FETA_SALAD_COURSE_TYPES), false)
        );
    }

}
