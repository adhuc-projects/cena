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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import org.adhuc.cena.menu.recipes.CreateRecipe;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A request to create a recipe.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Getter
@ToString
class CreateRecipeRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String content;

    /**
     * Converts this request to a {@code CreateRecipe} command.
     *
     * @param id the recipe identity.
     * @return the recipe creation command.
     */
    CreateRecipe toCommand(@NonNull RecipeId id) {
        return new CreateRecipe(id, name, content);
    }

}
