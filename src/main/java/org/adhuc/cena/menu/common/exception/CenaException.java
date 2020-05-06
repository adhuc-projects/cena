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
package org.adhuc.cena.menu.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Defines the root of the exceptions hierarchy in the Cena system.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CenaException extends RuntimeException implements CodifiedException {

    private static final ExceptionCode DEFAULT_CODE = ExceptionCode.INTERNAL_ERROR;

    private final ExceptionCode code;

    /**
     * Constructs a new {@code CenaException} with the specified code. The message is initialized with code description.
     * The cause is not initialized.
     *
     * @param code the exception code.
     */
    protected CenaException(ExceptionCode code) {
        this(null, null, code);
    }

    /**
     * Constructs a new {@code CenaException} with the specified detail message and code. The cause is not initialized.
     *
     * @param message the detail message.
     * @param code    the exception code.
     */
    protected CenaException(String message, ExceptionCode code) {
        this(message, null, code);
    }

    /**
     * Constructs a new {@code CenaException} with the specified detail message, cause and code.
     *
     * @param message the detail message.
     * @param cause   the cause.
     * @param code    the exception code.
     */
    protected CenaException(String message, Throwable cause, ExceptionCode code) {
        super(message != null ? message : code != null ? code.description() : DEFAULT_CODE.description(), cause);
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
        return DEFAULT_CODE;
    }

}
