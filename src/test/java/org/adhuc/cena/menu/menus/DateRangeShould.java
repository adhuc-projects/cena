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

import java.time.LocalDate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * The {@link DateRange} test class.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Tag("unit")
@Tag("domain")
@DisplayName("Date range should")
class DateRangeShould {

    @ParameterizedTest
    @MethodSource("invalidRanges")
    @DisplayName("not accept being built with invalid values")
    void notAcceptBeingBuiltWithInvalidValues(Supplier<DateRange> supplier) {
        assertThrows(IllegalArgumentException.class, supplier::get);
    }

    private static Stream<Arguments> invalidRanges() {
        return Stream.of(
                Arguments.of((Supplier<DateRange>) () -> since(null)),
                Arguments.of((Supplier<DateRange>) () -> until(null)),
                Arguments.of((Supplier<DateRange>) () -> until(now().minusDays(1))),
                Arguments.of((Supplier<DateRange>) () -> until(now().minusDays(1))),
                Arguments.of((Supplier<DateRange>) () -> range(now(), null)),
                Arguments.of((Supplier<DateRange>) () -> range(null, now())),
                Arguments.of((Supplier<DateRange>) () -> range(now().plusDays(1), now()))
        );
    }

    @ParameterizedTest
    @MethodSource("validRanges")
    @DisplayName("accept being built with valid values")
    void buildWithValidValues(Supplier<DateRange> supplier, LocalDate since, LocalDate until) {
        var range = supplier.get();
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(range.since()).isEqualTo(since);
            s.assertThat(range.until()).isEqualTo(until);
        });
    }

    private static Stream<Arguments> validRanges() {
        return Stream.of(
                Arguments.of((Supplier<DateRange>) DateRange::defaultRange, now(), now().plusDays(6)),
                Arguments.of((Supplier<DateRange>) () -> since(now()), now(), now().plusDays(6)),
                Arguments.of((Supplier<DateRange>) () -> since(now().minusDays(3)), now().minusDays(3), now().plusDays(3)),
                Arguments.of((Supplier<DateRange>) () -> since(now().plusDays(2)), now().plusDays(2), now().plusDays(8)),
                Arguments.of((Supplier<DateRange>) () -> until(now()), now(), now()),
                Arguments.of((Supplier<DateRange>) () -> until(now().plusDays(1)), now(), now().plusDays(1)),
                Arguments.of((Supplier<DateRange>) () -> until(now().plusMonths(1)), now(), now().plusMonths(1)),
                Arguments.of((Supplier<DateRange>) () -> range(now(), now()), now(), now()),
                Arguments.of((Supplier<DateRange>) () -> range(now(), now().plusDays(1)), now(), now().plusDays(1)),
                Arguments.of((Supplier<DateRange>) () -> range(now(), now().plusMonths(3)), now(), now().plusMonths(3))
        );
    }

    @Test
    @DisplayName("not accept builder with null since date")
    void notAcceptBuilderWithNullSince() {
        assertThrows(IllegalArgumentException.class, () -> builder().withSince(null));
    }

    @Test
    @DisplayName("not accept builder with null until date")
    void notAcceptBuilderWithNullUntil() {
        assertThrows(IllegalArgumentException.class, () -> builder().withUntil(null));
    }

    @ParameterizedTest
    @MethodSource("invalidBuilderSource")
    @DisplayName("not build query with until date lower than since date")
    void notBuildQueryWithUntilLowerThanSince(Builder builder) {
        assertThrows(IllegalArgumentException.class, builder::build);
    }

    private static Stream<Arguments> invalidBuilderSource() {
        return Stream.of(
                Arguments.of(builder().withUntil(now().minusDays(1))),
                Arguments.of(builder().withUntil(now()).withSince(now().plusDays(1)))
        );
    }

    @ParameterizedTest
    @MethodSource("validBuilderSource")
    @DisplayName("build query from valid builder")
    void buildQueryWithValidBuilder(Builder builder, LocalDate since, LocalDate until) {
        var range = builder.build();
        SoftAssertions.assertSoftly(s -> {
            s.assertThat(range.since()).isEqualTo(since);
            s.assertThat(range.until()).isEqualTo(until);
        });
    }

    private static Stream<Arguments> validBuilderSource() {
        return Stream.of(
                Arguments.of(builder(), now(), now().plusDays(6)),
                Arguments.of(builder().withSince(now()), now(), now().plusDays(6)),
                Arguments.of(builder().withSince(now().minusDays(1)), now().minusDays(1), now().plusDays(5)),
                Arguments.of(builder().withUntil(now()), now(), now()),
                Arguments.of(builder().withUntil(now().minusDays(1)).withSince(now().minusDays(1)),
                        now().minusDays(1), now().minusDays(1)),
                Arguments.of(builder().withSince(now().plusDays(1)).withUntil(now().plusWeeks(1)),
                        now().plusDays(1), now().plusWeeks(1))
        );
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("be equal when all attributes are equal")
    void isEqual(DateRange r1, DateRange r2, boolean expected) {
        assertThat(r1.equals(r2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalitySource")
    @DisplayName("have same hash code when all attributes are equal")
    void sameHashCode(DateRange r1, DateRange r2, boolean expected) {
        assertThat(r1.hashCode() == r2.hashCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> equalitySource() {
        var range = range(now().plusDays(1), now().plusDays(3));
        return Stream.of(
                Arguments.of(range, range, true),
                Arguments.of(range, range(now().plusDays(1), now().plusDays(3)), true),
                Arguments.of(range, range(now(), now().plusDays(3)), false),
                Arguments.of(range, range(now().plusDays(2), now().plusDays(3)), false),
                Arguments.of(range, range(now().plusDays(1), now().plusDays(2)), false),
                Arguments.of(range, range(now().plusDays(1), now().plusDays(4)), false)
        );
    }

}
