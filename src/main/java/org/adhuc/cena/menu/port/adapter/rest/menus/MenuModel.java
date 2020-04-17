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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.hateoas.RepresentationModel;

import org.adhuc.cena.menu.menus.Covers;
import org.adhuc.cena.menu.menus.MealType;
import org.adhuc.cena.menu.menus.Menu;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A REST resource encapsulating menu information.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@ToString(callSuper = true)
@Accessors(fluent = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class MenuModel extends RepresentationModel<MenuModel> {

    private final String date;
    private final String mealType;
    private final int covers;
    private final List<String> mainCourseRecipes;

    MenuModel(Menu original) {
        this(original.date(), original.mealType(), original.covers(), original.mainCourseRecipes());
    }

    MenuModel(LocalDate date, MealType mealType, Covers covers, Collection<RecipeId> mainCourseRecipes) {
        this.date = date.toString();
        this.mealType = mealType.name();
        this.covers = covers.value();
        this.mainCourseRecipes = mainCourseRecipes.stream().map(RecipeId::toString).collect(toList());
    }

}
