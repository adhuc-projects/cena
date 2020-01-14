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

import static org.adhuc.cena.menu.util.Assert.hasText;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.Name;

/**
 * A recipe creation command.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Value
@Accessors(fluent = true)
public class CreateRecipe {

    private final RecipeId recipeId;
    private final Name recipeName;
    private final String recipeContent;
    private final RecipeAuthor recipeAuthor;
    private final Servings servings;

    /**
     * Creates a recipe creation command.
     *
     * @param recipeId      the recipe identity.
     * @param recipeName    the recipe name.
     * @param recipeContent the recipe content.
     * @param recipeAuthor  the recipe author.
     * @param servings      the number of servings.
     */
    public CreateRecipe(@NonNull RecipeId recipeId, @NonNull Name recipeName, @NonNull String recipeContent,
                        @NonNull RecipeAuthor recipeAuthor, @NonNull Servings servings) {
        hasText(recipeContent, "Cannot create recipe creation command with invalid recipe content");
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeContent = recipeContent;
        this.recipeAuthor = recipeAuthor;
        this.servings = servings;
    }

    /**
     * Creates a recipe creation command with default value for number of servings.
     *
     * @param recipeId      the recipe identity.
     * @param recipeName    the recipe name.
     * @param recipeContent the recipe content.
     * @param recipeAuthor  the recipe author.
     */
    public CreateRecipe(@NonNull RecipeId recipeId, @NonNull Name recipeName, @NonNull String recipeContent,
                        @NonNull RecipeAuthor recipeAuthor) {
        this(recipeId, recipeName, recipeContent, recipeAuthor, Servings.DEFAULT);
    }

}
