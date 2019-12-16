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
package org.adhuc.cena.menu.recipes;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * An ingredient to recipe removal command.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Value
@Accessors(fluent = true)
public class RemoveIngredientFromRecipe {

    @NonNull
    private final IngredientId ingredientId;
    @NonNull
    private final RecipeId recipeId;

}
