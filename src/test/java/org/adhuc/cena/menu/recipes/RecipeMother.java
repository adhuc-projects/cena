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

import static java.util.stream.Collectors.toList;

import static lombok.AccessLevel.PRIVATE;

import static org.adhuc.cena.menu.ingredients.IngredientMother.CUCUMBER_ID;
import static org.adhuc.cena.menu.ingredients.IngredientMother.TOMATO_ID;
import static org.adhuc.cena.menu.recipes.MeasurementUnit.DOZEN;
import static org.adhuc.cena.menu.recipes.MeasurementUnit.UNIT;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

import org.adhuc.cena.menu.common.aggregate.Name;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientMother;

/**
 * An object mother to create testing domain elements related to {@link Recipe}s.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.2.0
 */
public class RecipeMother {

    public static final RecipeId TOMATO_CUCUMBER_MOZZA_SALAD_ID = new RecipeId("d71e2fc7-09e3-4241-97a5-dc3383d35e98");
    public static final Name TOMATO_CUCUMBER_MOZZA_SALAD_NAME = new Name("Tomato, cucumber and mozzarella salad");
    public static final String TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT = "Cut everything into dices, mix it, dress it";
    public static final RecipeAuthor TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR = new RecipeAuthor("some user");
    public static final Servings TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS = new Servings(2);

    public static final RecipeId TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID = new RecipeId("6ef45220-0e64-4303-9f71-ced6cefa6834");
    public static final Name TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME = new Name("Tomato, cucumber, olive and feta salad");
    public static final String TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT = "Stone olives, cut everything into dices, mix it, dress it";
    public static final RecipeAuthor TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR = new RecipeAuthor("other user");
    public static final Servings TOMATO_CUCUMBER_OLIVE_FETA_SALAD_SERVINGS = new Servings(6);

    public static final RecipeId ID = TOMATO_CUCUMBER_MOZZA_SALAD_ID;
    public static final Name NAME = TOMATO_CUCUMBER_MOZZA_SALAD_NAME;
    public static final String CONTENT = TOMATO_CUCUMBER_MOZZA_SALAD_CONTENT;
    public static final RecipeAuthor AUTHOR = TOMATO_CUCUMBER_MOZZA_SALAD_AUTHOR;
    public static final Servings SERVINGS = TOMATO_CUCUMBER_MOZZA_SALAD_SERVINGS;

    public static final boolean MAIN_INGREDIENT = true;

    public static final Quantity QUANTITY = new Quantity(1, DOZEN);

    public static CreateRecipe createCommand() {
        return createCommand(recipe());
    }

    public static CreateRecipe createCommand(@NonNull Recipe recipe) {
        return new CreateRecipe(recipe.id(), recipe.name(), recipe.content(), recipe.author(), recipe.servings());
    }

    public static DeleteRecipe deleteCommand() {
        return deleteCommand(TOMATO_CUCUMBER_MOZZA_SALAD_ID);
    }

    public static DeleteRecipe deleteCommand(@NonNull RecipeId recipeId) {
        return new DeleteRecipe(recipeId);
    }

    public static AddIngredientToRecipe addIngredientCommand() {
        return addIngredientCommand(recipeIngredient());
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull RecipeIngredient recipeIngredient) {
        return addIngredientCommand(recipeIngredient.ingredientId(), recipeIngredient.recipeId(),
                recipeIngredient.isMainIngredient(), recipeIngredient.quantity());
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull IngredientId ingredientId, boolean isMainIngredient) {
        return addIngredientCommand(ingredientId, ID, isMainIngredient, QUANTITY);
    }

    public static AddIngredientToRecipe addIngredientCommand(@NonNull IngredientId ingredientId,
                                                             @NonNull RecipeId recipeId,
                                                             boolean isMainIngredient,
                                                             @NonNull Quantity quantity) {
        return new AddIngredientToRecipe(ingredientId, recipeId, isMainIngredient, quantity);
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand() {
        return removeIngredientCommand(recipeIngredient());
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull RecipeIngredient recipeIngredient) {
        return removeIngredientCommand(recipeIngredient.ingredientId(), recipeIngredient.recipeId());
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull IngredientId ingredientId) {
        return removeIngredientCommand(ingredientId, ID);
    }

    public static RemoveIngredientFromRecipe removeIngredientCommand(@NonNull IngredientId ingredientId, @NonNull RecipeId recipeId) {
        return new RemoveIngredientFromRecipe(ingredientId, recipeId);
    }

    public static RemoveIngredientsFromRecipe removeIngredientsCommand() {
        return new RemoveIngredientsFromRecipe(ID);
    }

    public static Recipe recipe() {
        return builder().withIngredient(TOMATO_ID, MAIN_INGREDIENT, QUANTITY)
                .andIngredient(CUCUMBER_ID, false, new Quantity(3, UNIT)).build();
    }

    public static Collection<Recipe> recipes() {
        var tomatoCucumberMozzaSalad = recipe();
        var tomatoCucumberOliveAndFetaSalad = builder()
                .withId(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID)
                .withName(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME)
                .withContent(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT)
                .withAuthor(TOMATO_CUCUMBER_OLIVE_FETA_SALAD_AUTHOR)
                .withIngredients(TOMATO_ID)
                .build();
        return List.of(tomatoCucumberMozzaSalad, tomatoCucumberOliveAndFetaSalad);
    }

    public static RecipeIngredient recipeIngredient() {
        return recipeIngredient(IngredientMother.ID, MAIN_INGREDIENT, QUANTITY);
    }

    public static RecipeIngredient recipeIngredient(@NonNull IngredientId ingredientId,
                                                    boolean isMainIngredient,
                                                    @NonNull Quantity quantity) {
        return recipeIngredient(ID, ingredientId, isMainIngredient, quantity);
    }

    public static RecipeIngredient recipeIngredient(@NonNull RecipeId recipeId,
                                                    @NonNull IngredientId ingredientId,
                                                    boolean isMainIngredient,
                                                    @NonNull Quantity quantity) {
        return new RecipeIngredient(recipeId, ingredientId, isMainIngredient, quantity);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    public static class Builder {
        private RecipeId id = ID;
        @With
        private Name name = NAME;
        @With
        private String content = CONTENT;
        @With
        private RecipeAuthor author = AUTHOR;
        @With
        private Servings servings = SERVINGS;
        private List<RecipeIngredient> ingredients = List.of();

        public Builder withId(@NonNull RecipeId recipeId) {
            return new Builder(recipeId, name, content, author, servings,
                    ingredients.stream()
                            .map(ingredient -> new RecipeIngredient(recipeId, ingredient.ingredientId(),
                                    ingredient.isMainIngredient(), ingredient.quantity()))
                            .collect(toList()));
        }

        public Builder withAuthorName(@NonNull String authorName) {
            return new Builder(id, name, content, new RecipeAuthor(authorName), servings, ingredients);
        }

        public Builder withIngredients(@NonNull IngredientId... ingredientIds) {
            return new Builder(id, name, content, author, servings,
                    Arrays.stream(ingredientIds)
                            .map(ingredientId -> new RecipeIngredient(id, ingredientId, MAIN_INGREDIENT, QUANTITY))
                            .collect(toList()));
        }

        public Builder withIngredient(@NonNull IngredientId ingredientId, boolean isMainIngredient) {
            return new Builder(id, name, content, author, servings, List.of(new RecipeIngredient(id, ingredientId, isMainIngredient, QUANTITY)));
        }

        public Builder withIngredient(@NonNull IngredientId ingredientId, boolean isMainIngredient, @NonNull Quantity quantity) {
            return new Builder(id, name, content, author, servings, List.of(new RecipeIngredient(id, ingredientId, isMainIngredient, quantity)));
        }

        public Builder andIngredient(@NonNull IngredientId ingredientId, boolean isMainIngredient, @NonNull Quantity quantity) {
            var ingredients = new HashSet<>(this.ingredients);
            ingredients.add(new RecipeIngredient(id, ingredientId, isMainIngredient, quantity));
            return new Builder(id, name, content, author, servings, List.copyOf(ingredients));
        }

        public Recipe build() {
            var recipe = new Recipe(id, name, content, author, servings);
            ingredients.forEach(ingredient -> recipe.addIngredient(addIngredientCommand(ingredient)));
            return recipe;
        }
    }

}
