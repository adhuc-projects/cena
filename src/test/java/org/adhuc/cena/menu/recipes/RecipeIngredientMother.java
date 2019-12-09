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

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * An object mother to create testing domain elements related to {@link RecipeIngredient}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.2.0
 */
public class RecipeIngredientMother {

    public static final RecipeId RECIPE_ID = RecipeMother.ID;
    public static final IngredientId INGREDIENT_ID = IngredientMother.ID;

    public static RecipeIngredient recipeIngredient() {
        return recipeIngredient(RECIPE_ID, INGREDIENT_ID);
    }

    public static RecipeIngredient recipeIngredient(IngredientId ingredientId) {
        return recipeIngredient(RECIPE_ID, ingredientId);
    }

    public static RecipeIngredient recipeIngredient(RecipeId recipeId, IngredientId ingredientId) {
        return new RecipeIngredient(recipeId, ingredientId);
    }

}
