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
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import org.adhuc.cena.menu.menus.Covers;
import org.adhuc.cena.menu.menus.CreateMenu;
import org.adhuc.cena.menu.menus.MealType;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.port.adapter.rest.support.Uuids;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A request to create a menu.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@ToString
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CreateMenuRequest {

    @NotNull
    private LocalDate date;
    @NotNull
    private MealType mealType;
    @NotNull
    @Positive
    private Integer covers;
    @NotEmpty
    @Uuids
    private List<String> mainCourseRecipes;

    /**
     * Converts this request to a {@code CreateMenu} command.
     *
     * @param ownerName the menu owner name.
     * @return the menu creation command.
     */
    CreateMenu toCommand(@NonNull String ownerName) {
        return new CreateMenu(new MenuOwner(ownerName), date, mealType, new Covers(covers),
                mainCourseRecipes.stream().map(RecipeId::new).collect(toList()));
    }

}
