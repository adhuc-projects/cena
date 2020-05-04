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
package org.adhuc.cena.menu.menus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.MenuMother.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

/**
 * The {@link DeleteMenu} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menu deletion command should")
class DeleteMenuShould {

    @ParameterizedTest
    @NullSource
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(MenuId menuId) {
        assertThrows(IllegalArgumentException.class, () -> new DeleteMenu(menuId));
    }

    @Test
    @DisplayName("contain id used during creation")
    void containCreationValues() {
        var command = deleteCommand();
        assertThat(command.menuId()).isEqualTo(ID);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when menu id is equal")
    void isEqual(DeleteMenu c1, DeleteMenu c2, boolean expected) {
        assertThat(c1.equals(c2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when menu id is equal")
    void sameHashCode(DeleteMenu c1, DeleteMenu c2, boolean expected) {
        assertThat(c1.hashCode() == c2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var deleteTodayLunch = deleteCommand(TODAY_LUNCH_ID);
        return Stream.of(
                Arguments.of(deleteTodayLunch, deleteTodayLunch, true),
                Arguments.of(deleteTodayLunch, deleteCommand(TODAY_LUNCH_ID), true),
                Arguments.of(deleteTodayLunch, deleteCommand(TOMORROW_DINNER_ID), false)
        );
    }

}
