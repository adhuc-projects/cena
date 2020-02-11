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

import static java.util.stream.Collectors.toList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.List;

import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.adhuc.cena.menu.common.CenaException;
import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * An exception occurring while executing a rest-service call with invalid request.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@ResponseStatus(BAD_REQUEST)
class InvalidRestRequestException extends CenaException {

    private static final ExceptionCode EXCEPTION_CODE = ExceptionCode.INVALID_REQUEST;

    @NonNull
    private final Errors errors;

    /**
     * Constructs a new {@code InvalidRestRequestException} based on the specified validation errors.
     */
    InvalidRestRequestException(@NonNull Errors errors) {
        super(EXCEPTION_CODE);
        this.errors = errors;
    }

    /**
     * Gets the validation errors messages.
     *
     * @return the validation errors messages.
     */
    public List<String> getErrorMessages() {
        return errors.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(toList());
    }

}
