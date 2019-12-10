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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.BasicEntity;

/**
 * A recipe definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Recipe extends BasicEntity<RecipeId> {

    @Getter
    @NonNull
    private String name;
    @Getter
    @NonNull
    private String content;
    private Set<RecipeIngredient> ingredients = new HashSet<RecipeIngredient>();

    /**
     * Creates a recipe.
     *
     * @param id      the recipe identity.
     * @param name    the recipe name.
     * @param content the recipe content.
     */
    public Recipe(@NonNull RecipeId id, @NonNull String name, @NonNull String content) {
        super(id);
        hasText(name, "Cannot set recipe name with invalid value");
        hasText(content, "Cannot set recipe content with invalid value");
        this.name = name;
        this.content = content;
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param addIngredientCommand the command.
     */
    public void addIngredient(@NonNull AddIngredientToRecipe addIngredientCommand) {
        ingredients.add(new RecipeIngredient(id(), addIngredientCommand.ingredientId()));
    }

    public Set<RecipeIngredient> ingredients() {
        return Collections.unmodifiableSet(ingredients);
    }

}
