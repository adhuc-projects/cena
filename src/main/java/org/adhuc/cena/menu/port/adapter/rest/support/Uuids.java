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
package org.adhuc.cena.menu.port.adapter.rest.support;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.UUID;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * A validation constraint to ensure a list of values is either empty or contains only valid {@link UUID} values.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Documented
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = Uuids.UuidsConstraintValidator.class)
public @interface Uuids {

    String message() default "{common.Uuids.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propertyName() default "";

    class UuidsConstraintValidator implements ConstraintValidator<Uuids, List<String>> {
        @Override
        public boolean isValid(List<String> values, ConstraintValidatorContext context) {
            if (values == null || values.isEmpty()) {
                return true;
            }
            try {
                values.stream().forEach(value -> UUID.fromString(value));
                return true;
            } catch (final IllegalArgumentException e) {
                return false;
            }
        }
    }

}
