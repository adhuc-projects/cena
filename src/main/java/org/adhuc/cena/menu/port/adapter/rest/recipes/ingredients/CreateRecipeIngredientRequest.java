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
package org.adhuc.cena.menu.port.adapter.rest.recipes.ingredients;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.recipes.AddIngredientToRecipe;
import org.adhuc.cena.menu.recipes.MeasurementUnit;
import org.adhuc.cena.menu.recipes.Quantity;
import org.adhuc.cena.menu.recipes.RecipeId;

/**
 * A request to create a recipe ingredient.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@Getter
@ToString
@CreateRecipeIngredientRequest.ValidRequest
class CreateRecipeIngredientRequest {

    @NotBlank
    @JsonProperty("id")
    private String ingredientId;
    @JsonProperty("mainIngredient")
    private boolean isMainIngredient;
    @Positive
    private Integer quantity;
    private MeasurementUnit measurementUnit;

    /**
     * Converts this request to a {@code AddIngredientToRecipe} command.
     *
     * @param id the recipe identity.
     * @return the ingredient to recipe addition command..
     */
    AddIngredientToRecipe toCommand(@NonNull RecipeId id) {
        return new AddIngredientToRecipe(new IngredientId(ingredientId), id, isMainIngredient,
                quantity != null ? new Quantity(quantity, measurementUnit) : Quantity.UNDEFINED);
    }

    @Documented
    @Target(TYPE)
    @Retention(RUNTIME)
    @Constraint(validatedBy = RequestConstraintValidator.class)
    @interface ValidRequest {
        String message() default "{recipes.ingredients.CreateRecipeIngredient.ValidRequest.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    private static class RequestConstraintValidator implements ConstraintValidator<ValidRequest, CreateRecipeIngredientRequest> {
        @Override
        public boolean isValid(CreateRecipeIngredientRequest value, ConstraintValidatorContext context) {
            return value.quantity == null && value.measurementUnit == null ||
                    value.quantity != null && value.measurementUnit != null;
        }
    }

}
