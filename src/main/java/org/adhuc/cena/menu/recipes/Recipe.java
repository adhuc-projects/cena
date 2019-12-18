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
import static org.adhuc.cena.menu.util.Assert.isTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.BasicEntity;
import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientId;

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
    private Set<RecipeIngredient> ingredients = new HashSet<>();

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
     * Gets the set of ingredients composing this recipe. This set is immutable.
     *
     * @return the ingredients composing the recipe.
     */
    public Set<RecipeIngredient> ingredients() {
        return Collections.unmodifiableSet(ingredients);
    }

    /**
     * Gets the ingredient corresponding to the specified identity from the set of ingredients.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient.
     * @throws EntityNotFoundException if the ingredient could not be found from the set of ingredients.
     */
    public RecipeIngredient ingredient(IngredientId ingredientId) {
        return maybeIngredient(ingredientId).orElseThrow(() -> new EntityNotFoundException(Ingredient.class, ingredientId));
    }

    private Optional<RecipeIngredient> maybeIngredient(IngredientId ingredientId) {
        return ingredients.stream().filter(i -> i.ingredientId().equals(ingredientId)).findFirst();
    }

    /**
     * Adds an ingredient to the recipe.
     *
     * @param command the command.
     * @throws IllegalArgumentException if the command's recipe identity does not correspond to this recipe identity.
     */
    void addIngredient(@NonNull AddIngredientToRecipe command) {
        isTrue(id().equals(command.recipeId()),
                () -> String.format("Wrong command recipe identity %s to add ingredient to recipe with identity %s",
                        command.recipeId(), id()));
        ingredients.add(new RecipeIngredient(id(), command.ingredientId()));
    }

    /**
     * Removes an ingredient from the recipe.
     *
     * @param command the command.
     * @throws IllegalArgumentException if the command's recipe identity does not correspond to this recipe identity.
     * @throws IngredientNotRelatedToRecipeException if the ingredient is not related to the recipe.
     */
    void removeIngredient(@NonNull RemoveIngredientFromRecipe command) {
        isTrue(id().equals(command.recipeId()),
                () -> String.format("Wrong command recipe identity %s to remove ingredient from recipe with identity %s",
                        command.recipeId(), id()));
        var ingredient = maybeIngredient(command.ingredientId()).orElseThrow(() ->
                new IngredientNotRelatedToRecipeException(command.ingredientId(), command.recipeId()));
        ingredients.remove(ingredient);
    }

    /**
     * Removes all ingredients from the recipe.
     *
     * @param command the command.
     * @throws IllegalArgumentException if the command's recipe identity does not correspond to this recipe identity.
     */
    void removeIngredients(@NonNull RemoveIngredientsFromRecipe command) {
        isTrue(id().equals(command.recipeId()),
                () -> String.format("Wrong command recipe identity %s to remove ingredients from recipe with identity %s",
                        command.recipeId(), id()));
        ingredients.clear();
    }

}
