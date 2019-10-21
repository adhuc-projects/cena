package org.adhuc.cena.menu.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link CreateIngredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient creation command should")
class CreateIngredientShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId, String name) {
        assertThrows(IllegalArgumentException.class, () -> new CreateIngredient(ingredientId, name));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, NAME),
                Arguments.of(ID, null),
                Arguments.of(ID, "")
        );
    }

    @Test
    @DisplayName("contain id and name used during creation")
    void containCreationValues() {
        var command = createCommand();
        assertSoftly(softly -> {
            softly.assertThat(command.ingredientId()).isEqualTo(ID);
            softly.assertThat(command.ingredientName()).isEqualTo(NAME);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when ingredient id and name are equal")
    void isEqual(CreateIngredient c1, CreateIngredient c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id and name are equal")
    void sameHashCode(CreateIngredient c1, CreateIngredient c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var createTomato = createCommand(ingredient(TOMATO_ID, TOMATO));
        return Stream.of(
                Arguments.of(createTomato, createTomato, true),
                Arguments.of(createTomato, createCommand(ingredient(TOMATO_ID, TOMATO)), true),
                Arguments.of(createTomato, createCommand(ingredient(TOMATO_ID, CUCUMBER)), false),
                Arguments.of(createTomato, createCommand(ingredient(CUCUMBER_ID, TOMATO)), false)
        );
    }

}
