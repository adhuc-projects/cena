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

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.util.Assert;

/**
 * A date range, consisting of a lower bound and an upper bound, where upper bound is at least equal to lower bound.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Value
@Getter(AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class DateRange {
    @NonNull
    private final LocalDate since;
    @NonNull
    private final LocalDate until;

    /**
     * Builds a default date range, with lower bound set to today and upper bound set to today + 6 days.
     */
    public static DateRange defaultRange() {
        return since(defaultSince());
    }

    /**
     * Builds a date range, with lower bound set to specified value and upper bound set to {@code since} + 6 days.
     *
     * @param since the date range lower bound.
     */
    public static DateRange since(@NonNull LocalDate since) {
        return range(since, since.plusDays(6));
    }

    /**
     * Builds a date range, with lower bound set to today and upper bound set specified value.
     *
     * @param until the date range upper bound. Cannot be lower than today.
     */
    public static DateRange until(@NonNull LocalDate until) {
        return range(defaultSince(), until);
    }

    /**
     * Builds a date range, with specified lower and upper bounds.
     *
     * @param since the date range lower bound. Cannot be higher than upper bound.
     * @param until the date range upper bound. Cannot be lower than lower bound.
     */
    public static DateRange range(@NonNull LocalDate since, @NonNull LocalDate until) {
        Assert.isTrue(until.compareTo(since) >= 0,
                () -> "Cannot build menus listing query with until " + until + " lower than since " + since);
        return new DateRange(since, until);
    }

    private static LocalDate defaultSince() {
        return LocalDate.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        @With
        @NonNull
        private LocalDate since = defaultSince();
        private LocalDate until;

        public Builder withUntil(@NonNull LocalDate until) {
            return new Builder(since, until);
        }

        public DateRange build() {
            return until != null ? range(since, until) : since(since);
        }
    }
}
