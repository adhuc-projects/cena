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

import static java.time.LocalDate.now;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.adhuc.cena.menu.menus.DateRange.*;
import static org.adhuc.cena.menu.menus.MenuMother.OTHER_OWNER;
import static org.adhuc.cena.menu.menus.MenuMother.OWNER;

import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link ListMenus} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Menus listing query should")
class ListMenusShould {

    @ParameterizedTest
    @MethodSource("invalidCreationParameters")
    @DisplayName("not be creatable with invalid parameters")
    void notBeCreatableWithInvalidParameters(MenuOwner owner, DateRange range) {
        assertThrows(IllegalArgumentException.class, () -> new ListMenus(owner, range));
    }

    private static Stream<Arguments> invalidCreationParameters() {
        return Stream.of(
                Arguments.of(null, defaultRange()),
                Arguments.of(OWNER, null)
        );
    }

    @Test
    @DisplayName("contain value used during construction")
    void containCreationValue() {
        var since = now().minusDays(1);
        var until = now().plusDays(3);
        var query = new ListMenus(OWNER, range(since, until));
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(query.owner()).isEqualTo(OWNER);
            s.assertThat(query.since()).isEqualTo(since);
            s.assertThat(query.until()).isEqualTo(until);
        });
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when all values are equal")
    void isEqual(ListMenus q1, ListMenus q2, boolean expected) {
        assertThat(q1.equals(q2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when all value are equal")
    void sameHashCode(ListMenus q1, ListMenus q2, boolean expected) {
        assertThat(q1.hashCode() == q2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var query = new ListMenus(OWNER, defaultRange());
        return Stream.of(
                Arguments.of(query, query, true),
                Arguments.of(query, new ListMenus(OWNER, defaultRange()), true),
                Arguments.of(query, new ListMenus(OTHER_OWNER, defaultRange()), false),
                Arguments.of(query, new ListMenus(OWNER, since(now().plusDays(1))), false),
                Arguments.of(query, new ListMenus(OWNER, until(now().plusDays(3))), false)
        );
    }

}
