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
package org.adhuc.cena.menu.steps.menus;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import cucumber.api.Transformer;

import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationType;

/**
 * A {@link Transformer} implementation for {@link AuthenticationType}s.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
public class MenuDateTransformer extends Transformer<LocalDate> {

    private static final Map<Pattern, BiFunction<MatchResult, String, LocalDate>> transformations = Map.of(
            Pattern.compile("^day before yesterday$"), (result, date) -> LocalDate.now().minusDays(2),
            Pattern.compile("^yesterday$"), (result, date) -> LocalDate.now().minusDays(1),
            Pattern.compile("^today$"), (result, date) -> LocalDate.now(),
            Pattern.compile("^tomorrow$"), (result, date) -> LocalDate.now().plusDays(1),
            Pattern.compile("^day after tomorrow$"), (result, date) -> LocalDate.now().plusDays(2),
            Pattern.compile("^today \\+ (\\d+)$"), (result, date) -> LocalDate.now().plusDays(Integer.parseInt(result.group(1))),
            Pattern.compile("^20\\d{2}-\\d{2}-\\d{2}$"), (result, date) -> LocalDate.parse(date)
    );

    @Override
    public LocalDate transform(String value) {
        var result = transformations.entrySet().stream()
                .filter(t -> t.getKey().matcher(value).matches())
                .findFirst().orElseThrow(() -> new AssertionError("Cannot parse \"" + value + "\" as a date"));
        var matcher = result.getKey().matcher(value);
        matcher.matches();
        return result.getValue().apply(matcher.toMatchResult(), value);
    }

}
