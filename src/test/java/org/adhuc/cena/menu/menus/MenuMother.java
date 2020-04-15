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

import static java.util.stream.Collectors.toSet;

import static lombok.AccessLevel.PRIVATE;

import static org.adhuc.cena.menu.recipes.RecipeMother.TOMATO_CUCUMBER_MOZZA_SALAD_ID;
import static org.adhuc.cena.menu.recipes.RecipeMother.TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * An object mother to create testing domain elements related to {@link Menu}s.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.3.0
 */
public class MenuMother {

    public static final MenuOwner OWNER = new MenuOwner("some owner");
    public static final MenuOwner OTHER_OWNER = new MenuOwner("other owner");

    public static final MenuOwner TODAY_LUNCH_OWNER = OWNER;
    public static final LocalDate TODAY_LUNCH_DATE = LocalDate.now();
    public static final MealType TODAY_LUNCH_MEAL_TYPE = MealType.LUNCH;
    public static final Covers TODAY_LUNCH_COVERS = new Covers(2);
    public static final Set<RecipeId> TODAY_LUNCH_MAIN_COURSE_RECIPES = Set.of(TOMATO_CUCUMBER_MOZZA_SALAD_ID);
    public static final MenuId TODAY_LUNCH_ID = new MenuId(TODAY_LUNCH_OWNER, TODAY_LUNCH_DATE, TODAY_LUNCH_MEAL_TYPE);

    public static final MenuOwner TOMORROW_DINNER_OWNER = TODAY_LUNCH_OWNER;
    public static final LocalDate TOMORROW_DINNER_DATE = LocalDate.now().plusDays(1);
    public static final MealType TOMORROW_DINNER_MEAL_TYPE = MealType.DINNER;
    public static final Covers TOMORROW_DINNER_COVERS = new Covers(4);
    public static final Set<RecipeId> TOMORROW_DINNER_MAIN_COURSE_RECIPES = Set.of(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID);
    public static final MenuId TOMORROW_DINNER_ID = new MenuId(TOMORROW_DINNER_OWNER, TOMORROW_DINNER_DATE, TOMORROW_DINNER_MEAL_TYPE);

    public static final LocalDate DATE = TODAY_LUNCH_DATE;
    public static final MealType MEAL_TYPE = TODAY_LUNCH_MEAL_TYPE;
    public static final Covers COVERS = TODAY_LUNCH_COVERS;
    public static final Set<RecipeId> MAIN_COURSE_RECIPES = TODAY_LUNCH_MAIN_COURSE_RECIPES;
    public static final MenuId ID = TODAY_LUNCH_ID;

    public static CreateMenu createCommand() {
        return createCommand(menu());
    }

    public static CreateMenu createCommand(@NonNull Menu menu) {
        return new CreateMenu(menu.owner(), menu.date(), menu.mealType(), menu.covers(), menu.mainCourseRecipes());
    }

    public static Menu menu() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    public static class Builder {
        @With
        private MenuOwner owner = OWNER;
        @With
        private LocalDate date = DATE;
        @With
        private MealType mealType = MEAL_TYPE;
        @With
        private Covers covers = COVERS;
        private Set<RecipeId> mainCourseRecipes = Set.copyOf(MAIN_COURSE_RECIPES);

        public MenuMother.Builder withOwnerName(@NonNull String ownerName) {
            return new MenuMother.Builder(new MenuOwner(ownerName), date, mealType, covers, mainCourseRecipes);
        }

        public MenuMother.Builder withMainCourseRecipes(@NonNull RecipeId... recipeIds) {
            return new MenuMother.Builder(owner, date, mealType, covers,
                    Arrays.stream(recipeIds).collect(toSet()));
        }

        public MenuMother.Builder withMainCourseRecipes(@NonNull Collection<RecipeId> recipeIds) {
            return new MenuMother.Builder(owner, date, mealType, covers, Set.copyOf(recipeIds));
        }

        public Menu build() {
            return new Menu(new MenuId(owner, date, mealType), covers, mainCourseRecipes);
        }
    }

}
