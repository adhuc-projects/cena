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
package org.adhuc.cena.menu.steps.serenity.menus;

import static java.util.stream.Collectors.toList;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.steps.serenity.recipes.RecipeValue;
import org.adhuc.cena.menu.steps.serenity.support.resource.HateoasClientResourceSupport;
import org.adhuc.cena.menu.util.ListComparator;

/**
 * A menu value on the client side.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Data
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor(access = PRIVATE)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(NON_EMPTY)
public class MenuValue extends HateoasClientResourceSupport {

    public static final String DATE_FIELD = "date";
    public static final String MEAL_TYPE_FIELD = "mealType";
    public static final String COVERS_FIELD = "covers";
    public static final String MAIN_COURSE_RECIPES_FIELD = "mainCourseRecipes";

    public static final Comparator<MenuValue> COMPARATOR = Comparator.comparing(MenuValue::date)
            .thenComparing(MenuValue::mealType)
            .thenComparing(MenuValue::covers)
            .thenComparing(MenuValue::mainCourseRecipes, new ListComparator<>());

    private final LocalDate date;
    private final String mealType;
    private final Integer covers;
    private final List<String> mainCourseRecipes;

    public static MenuValue buildUnknownMenuValue(LocalDate date, String mealType, String menusResourceUrl) {
        var menu = builder().withDate(date).withMealType(mealType).build();
        menu.addLink(SELF_LINK, String.format("%s/%s-%s", menusResourceUrl, menu.date, menu.mealType));
        return menu;
    }

    boolean hasSameScheduleAs(@NonNull MenuValue menu) {
        return menu.date != null && menu.mealType != null && isScheduledAt(menu.date, menu.mealType);
    }

    boolean isScheduledAt(@NonNull LocalDate date, @NonNull String mealType) {
        return date.equals(this.date) && mealType.equalsIgnoreCase(this.mealType);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    public static class Builder {
        @With
        private LocalDate date = LocalDate.now();
        private String mealType = "LUNCH";
        @With
        private Integer covers = 2;
        private List<String> mainCourseRecipes = Collections.emptyList();

        public Builder withMealType(String mealType) {
            return new Builder(date, mealType != null ? mealType.toUpperCase() : null, covers, mainCourseRecipes);
        }

        public Builder withNoMainCourseRecipes() {
            return new Builder(date, mealType, covers, null);
        }

        public Builder withMainCourseRecipes(@NonNull RecipeValue... recipes) {
            return new Builder(date, mealType, covers, Arrays.stream(recipes).map(RecipeValue::id).collect(toList()));
        }

        public Builder withMainCourseRecipes(Collection<RecipeValue> recipes) {
            return new Builder(date, mealType, covers, recipes.stream().map(RecipeValue::id).collect(toList()));
        }

        public Builder withMainCourseRecipes(@NonNull String... recipeIds) {
            return new Builder(date, mealType, covers, Arrays.asList(recipeIds));
        }

        public MenuValue build() {
            return new MenuValue(date, mealType, covers, mainCourseRecipes);
        }
    }
}
