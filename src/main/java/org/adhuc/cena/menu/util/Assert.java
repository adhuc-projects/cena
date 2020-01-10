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
package org.adhuc.cena.menu.util;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collection;
import java.util.function.Supplier;

import lombok.NoArgsConstructor;

/**
 * An assertion utility class, used to avoid direct dependency between domain classes and the Spring
 * {@link org.springframework.util.Assert} utility class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class Assert {

    /**
     * Assert that an object is not {@code null}.
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the object is {@code null}
     * @see org.springframework.util.Assert#notNull(Object, String)
     */
    public static void notNull(Object object, String message) {
        org.springframework.util.Assert.notNull(object, message);
    }

    /**
     * Assert that the given String contains valid text content; that is, it must not
     * be {@code null} and must contain at least one non-whitespace character.
     * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
     * @param text the String to check
     * @param message the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the text does not contain valid text content
     * @see org.springframework.util.Assert#hasText(String, String)
     */
    public static void hasText(String text, String message) {
        org.springframework.util.Assert.hasText(text, message);
    }

    /**
     * Assert a boolean expression, throwing an {@code IllegalArgumentException}
     * if the expression evaluates to {@code false}.
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, () -&gt; "The value '" + i + "' must be greater than zero");
     * </pre>
     * @param expression a boolean expression
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if {@code expression} is {@code false}
     * @see org.springframework.util.Assert#isTrue(boolean, Supplier)
     */
    public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
        org.springframework.util.Assert.isTrue(expression, messageSupplier);
    }

    /**
     * Assert that a collection contains elements; that is, it must not be
     * {@code null} and must contain at least one element.
     * <pre class="code">
     * Assert.notEmpty(collection, () -&gt; "The " + collectionType + " collection must contain elements");
     * </pre>
     *
     * @param collection      the collection to check
     * @param messageSupplier a supplier for the exception message to use if the assertion fails
     * @throws IllegalArgumentException if the collection is {@code null} or contains no elements
     * @see org.springframework.util.Assert#notEmpty(Collection, Supplier)
     */
    public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
        org.springframework.util.Assert.notEmpty(collection, messageSupplier);
    }

}
