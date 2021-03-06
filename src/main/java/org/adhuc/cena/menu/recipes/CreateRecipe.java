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
package org.adhuc.cena.menu.recipes;

import static org.adhuc.cena.menu.util.Assert.hasText;

import java.util.Set;

import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.aggregate.Command;
import org.adhuc.cena.menu.common.aggregate.Name;

/**
 * A recipe creation command.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Command
@Value
@Accessors(fluent = true)
public class CreateRecipe {

    private final RecipeId recipeId;
    private final Name recipeName;
    private final String recipeContent;
    private final RecipeAuthor recipeAuthor;
    private final Servings servings;
    private final Set<CourseType> courseTypes;

    /**
     * Creates a recipe creation command.
     *
     * @param recipeId      the recipe identity.
     * @param recipeName    the recipe name.
     * @param recipeContent the recipe content.
     * @param recipeAuthor  the recipe author.
     * @param servings      the number of servings.
     * @param courseTypes   the course types.
     */
    public CreateRecipe(@NonNull RecipeId recipeId, @NonNull Name recipeName, @NonNull String recipeContent,
                        @NonNull RecipeAuthor recipeAuthor, @NonNull Servings servings, @NonNull Set<CourseType> courseTypes) {
        hasText(recipeContent, "Cannot create recipe creation command with invalid recipe content");
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.recipeContent = recipeContent;
        this.recipeAuthor = recipeAuthor;
        this.servings = servings;
        this.courseTypes = Set.copyOf(courseTypes);
    }

    /**
     * Creates a recipe creation command with default value for number of servings.
     *
     * @param recipeId      the recipe identity.
     * @param recipeName    the recipe name.
     * @param recipeContent the recipe content.
     * @param recipeAuthor  the recipe author.
     * @param courseTypes   the course types.
     */
    public CreateRecipe(@NonNull RecipeId recipeId, @NonNull Name recipeName, @NonNull String recipeContent,
                        @NonNull RecipeAuthor recipeAuthor, @NonNull Set<CourseType> courseTypes) {
        this(recipeId, recipeName, recipeContent, recipeAuthor, Servings.DEFAULT, courseTypes);
    }

}
