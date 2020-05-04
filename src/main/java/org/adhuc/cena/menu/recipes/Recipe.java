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
import static org.adhuc.cena.menu.util.Assert.isTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import org.adhuc.cena.menu.common.BasicEntity;
import org.adhuc.cena.menu.common.EntityNotFoundException;
import org.adhuc.cena.menu.common.Name;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientId;

/**
 * A recipe definition.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class Recipe extends BasicEntity<RecipeId> {

    @Getter
    @NonNull
    private Name name;
    @Getter
    @NonNull
    private String content;
    @Getter
    @NonNull
    private RecipeAuthor author;
    @Getter
    private Servings servings;
    private Map<IngredientId, RecipeIngredient> ingredients = new HashMap<>();

    /**
     * Creates a recipe based on the specified creation command.
     *
     * @param command the recipe creation command.
     */
    public Recipe(@NonNull CreateRecipe command) {
        this(command.recipeId(), command.recipeName(), command.recipeContent(), command.recipeAuthor(), command.servings());
    }

    /**
     * Creates a recipe.
     *
     * @param id       the recipe identity.
     * @param name     the recipe name.
     * @param content  the recipe content.
     * @param author   the recipe author.
     * @param servings the number of servings for recipe.
     */
    Recipe(@NonNull RecipeId id, @NonNull Name name, @NonNull String content, @NonNull RecipeAuthor author,
                  @NonNull Servings servings) {
        super(id);
        hasText(content, "Cannot set recipe content with invalid value");
        this.name = name;
        this.content = content;
        this.author = author;
        this.servings = servings;
    }

    /**
     * Indicates whether the recipe is composed of the ingredient corresponding to the specified identity.
     *
     * @param ingredientId the ingredient identity.
     * @return {@code true} if the ingredient identity has been add to the recipe composition, {@code false} otherwise.
     * @see #addIngredient(AddIngredientToRecipe)
     */
    public boolean isComposedOf(IngredientId ingredientId) {
        return ingredients.containsKey(ingredientId);
    }

    /**
     * Gets the set of ingredients composing this recipe. This set is immutable.
     *
     * @return the ingredients composing the recipe.
     */
    public Set<RecipeIngredient> ingredients() {
        return Set.copyOf(ingredients.values());
    }

    /**
     * Gets the ingredient corresponding to the specified identity from the set of ingredients.
     *
     * @param ingredientId the ingredient identity.
     * @return the ingredient.
     * @throws EntityNotFoundException if the ingredient could not be found from the set of ingredients.
     */
    public RecipeIngredient ingredient(IngredientId ingredientId) {
        if (isComposedOf(ingredientId)) {
            return ingredients.get(ingredientId);
        }
        throw new EntityNotFoundException(Ingredient.class, ingredientId);
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
        ingredients.put(command.ingredientId(), new RecipeIngredient(id(), command.ingredientId(),
                command.isMainIngredient(), command.quantity()));
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
        if (ingredients.remove(command.ingredientId()) == null) {
            throw new IngredientNotRelatedToRecipeException(command.ingredientId(), command.recipeId());
        }
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
