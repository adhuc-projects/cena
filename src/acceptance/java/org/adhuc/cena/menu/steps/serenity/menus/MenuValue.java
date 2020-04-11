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
package org.adhuc.cena.menu.steps.serenity.menus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;

/**
 * A menu value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Data
@EqualsAndHashCode
@ToString(includeFieldNames = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class MenuValue {

    private final LocalDate date;
    private final String mealType;
    private final int covers;
    private final List<String> mainCourseRecipes;

    public MenuValue(LocalDate date, String mealType, int covers, RecipeValue recipe) {
        this(date, mealType.toUpperCase(), covers, List.of(recipe.id()));
    }

    void assertEqualTo(MenuValue expected) {
        assertThat(this).isEqualTo(expected);
    }
}
