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

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.util.Assert;

/**
 * A menu creation command.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Value
@Getter(AccessLevel.PACKAGE)
@Accessors(fluent = true)
public class CreateMenu {

    @NonNull
    private final MenuId menuId;
    @NonNull
    private final Covers covers;
    @NonNull
    private final Set<RecipeId> mainCourseRecipes;

    /**
     * Creates a menu creation command.
     *
     * @param owner             the menu owner.
     * @param date              the menu date.
     * @param mealType          the menu meal type.
     * @param covers            the menu number of covers.
     * @param mainCourseRecipes the menu main course recipes.
     */
    public CreateMenu(@NonNull MenuOwner owner, @NonNull LocalDate date, @NonNull MealType mealType,
                      @NonNull Covers covers, @NonNull Collection<RecipeId> mainCourseRecipes) {
        Assert.notEmpty(mainCourseRecipes, () -> "Cannot create menu creation command without any main course recipe");
        menuId = new MenuId(owner, date, mealType);
        this.covers = covers;
        this.mainCourseRecipes = Set.copyOf(mainCourseRecipes);
    }

}
