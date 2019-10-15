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

import java.util.Map;
import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import org.adhuc.cena.menu.common.CenaException;
import org.adhuc.cena.menu.common.ExceptionCode;

/**
 * An {@link org.springframework.boot.web.servlet.error.ErrorAttributes ErrorAttributes} implementation based on the
 * {@link DefaultErrorAttributes} implementation, that provides the {@link ExceptionCode} value in the response.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Component
class RestControllerErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        var errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        addExceptionCode(errorAttributes, webRequest);
        return errorAttributes;
    }

    private void addExceptionCode(Map<String, Object> errorAttributes, WebRequest webRequest) {
        var error = getError(webRequest);
        if (error != null) {
            while (ServletException.class.isAssignableFrom(error.getClass()) && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
            addExceptionCode(errorAttributes, error);
        }
    }

    private void addExceptionCode(Map<String, Object> errorAttributes, Throwable error) {
        var exceptionCode = ExceptionCode.INTERNAL_ERROR;
        if (CenaException.class.isAssignableFrom(error.getClass())) {
            var cenaException = (CenaException) error;
            exceptionCode = cenaException.exceptionCode();
        }
        errorAttributes.put("code", exceptionCode.code());
    }

}