package org.adhuc.cena.menu.ingredients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.ingredients.IngredientMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

/**
 * The {@link DeleteIngredient} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Ingredient deletion command should")
class DeleteIngredientShould {

    @ParameterizedTest
    @NullSource
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(IngredientId ingredientId) {
        assertThrows(IllegalArgumentException.class, () -> new DeleteIngredient(ingredientId));
    }

    @Test
    @DisplayName("contain id used during creation")
    void containCreationValues() {
        var command = deleteCommand();
        assertThat(command.ingredientId()).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when ingredient id is equal")
    void isEqual(DeleteIngredient c1, DeleteIngredient c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when ingredient id is equal")
    void sameHashCode(DeleteIngredient c1, DeleteIngredient c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var deleteTomato = deleteCommand(TOMATO_ID);
        return Stream.of(
                Arguments.of(deleteTomato, deleteTomato, true),
                Arguments.of(deleteTomato, deleteCommand(TOMATO_ID), true),
                Arguments.of(deleteTomato, deleteCommand(CUCUMBER_ID), false)
        );
    }

}
