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
import java.util.Collections;
import java.util.Set;

import lombok.*;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.entity.Entity;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A menu definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Getter
@Accessors(fluent = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(of = {"id"})
@ToString
public class Menu implements Entity<MenuId> {

    @NonNull
    private final MenuId id;
    @NonNull
    private Covers covers;
    @NonNull
    private Set<RecipeId> mainCourseRecipes;

    /**
     * Creates a menu based on the specified creation command.
     *
     * @param command the menu creation command.
     */
    public Menu(@NonNull CreateMenu command) {
        this(command.menuId(), command.covers(), command.mainCourseRecipes());
    }

    public MenuOwner owner() {
        return id.owner();
    }

    public LocalDate date() {
        return id.date();
    }

    public MealType mealType() {
        return id.mealType();
    }

    public Set<RecipeId> mainCourseRecipes() {
        return Collections.unmodifiableSet(mainCourseRecipes);
    }

}
