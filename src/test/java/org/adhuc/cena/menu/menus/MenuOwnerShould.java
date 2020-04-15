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
package org.adhuc.cena.menu.menus;

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
 * The {@link MenuOwner} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menu owner should")
class MenuOwnerShould {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("not be creatable from invalid value")
    void notBeCreatableFromInvalidValue(String ownerName) {
        assertThrows(IllegalArgumentException.class, () -> new MenuOwner(ownerName));
    }

    @Test
    @DisplayName("contain owner name used during creation")
    void containCreationValues() {
        var owner = new MenuOwner("some owner");
        assertThat(owner.ownerName()).isEqualTo("some owner");
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when owner name is equal")
    void isEqual(MenuOwner o1, MenuOwner o2, boolean expected) {
        assertThat(o1.equals(o2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when owner name is equal")
    void sameHashCode(MenuOwner o1, MenuOwner o2, boolean expected) {
        assertThat(o1.hashCode() == o2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var menuOwner = new MenuOwner("some owner");
        return Stream.of(
                Arguments.of(menuOwner, menuOwner, true),
                Arguments.of(menuOwner, new MenuOwner("some owner"), true),
                Arguments.of(menuOwner, new MenuOwner("other owner"), false)
        );
    }

    @Test
    @DisplayName("return owner name during toString")
    void returnOwnerNameOnToString() {
        assertThat(new MenuOwner("some owner").toString()).isEqualTo("some owner");
    }

}
