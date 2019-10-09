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
package org.adhuc.cena.menu.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Defines the root of the exceptions hierarchy in the Cena system.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CenaException extends RuntimeException implements CodifiedException {

    private final ExceptionCode code;

    /**
     * Constructs a new {@code CenaException} with the specified detail message. The cause is not initialized, and the
     * code is set as default.
     *
     * @param message the detail message.
     */
    protected CenaException(final String message) {
        this(message, (Throwable) null);
    }

    /**
     * Constructs a new {@code CenaException} with the specified cause. The message is not initialized. The code is set
     * as default, or as the cause code if the cause is a {@link CodifiedException}.
     *
     * @param cause the cause.
     */
    protected CenaException(final Throwable cause) {
        this(cause, translateToCode(cause));
    }

    /**
     * Constructs a new {@code CenaException} with the specified code. The message is initialized with code description.
     * The cause is not initialized.
     *
     * @param code the exception code.
     */
    protected CenaException(final ExceptionCode code) {
        this(code != null ? code.description() : null, null, code);
    }

    /**
     * Constructs a new {@code CenaException} with the specified detail message and cause. The code is set as default,
     * or as the cause code if the cause is a {@link CodifiedException}.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    protected CenaException(final String message, final Throwable cause) {
        this(message, cause, cause != null ? translateToCode(cause) : null);
    }

    /**
     * Constructs a new {@code CenaException} with the specified detail message and code. The cause is not initialized.
     *
     * @param message the detail message.
     * @param code    the exception code.
     */
    protected CenaException(final String message, final ExceptionCode code) {
        this(message, null, code);
    }

    /**
     * Constructs a new {@code CenaException} with the specified cause and code. The message is not initialized.
     *
     * @param cause the cause.
     * @param code  the exception code.
     */
    protected CenaException(final Throwable cause, final ExceptionCode code) {
        this(cause.getMessage(), cause, code);
    }

    /**
     * Constructs a new {@code CenaException} with the specified detail message, cause and code.
     *
     * @param message the detail message.
     * @param cause   the cause.
     * @param code    the exception code.
     */
    protected CenaException(final String message, final Throwable cause, final ExceptionCode code) {
        super(message, cause);
        this.code = code != null ? code : defaultCode();
    }

    @Override
    public final ExceptionCode exceptionCode() {
        return code;
    }

    /**
     * Gets the default exception code for this kind of exception.
     *
     * @return the default exception code.
     */
    protected ExceptionCode defaultCode() {
        return ExceptionCode.INTERNAL_ERROR;
    }

    /**
     * Gets the exception code depending on the cause, recursively trying to get code from a {@link CodifiedException}
     * cause. Returns {@code null} by default.
     *
     * @param cause the cause exception.
     * @return the exception code corresponding to the cause (default is {@code null}).
     */
    private static ExceptionCode translateToCode(final Throwable cause) {
        if (CodifiedException.class.isAssignableFrom(cause.getClass())) {
            return ((CodifiedException) cause).exceptionCode();
        } else if (cause.getCause() != null) {
            // Recursively try to get exception code from cause
            return translateToCode(cause.getCause());
        }
        return null;
    }

}
