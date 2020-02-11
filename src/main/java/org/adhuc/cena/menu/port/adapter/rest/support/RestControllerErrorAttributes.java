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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;

import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
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
 * @version 0.2.0
 * @since 0.1.0
 */
@Slf4j
@Component
class RestControllerErrorAttributes extends DefaultErrorAttributes {

    private static final String CODE_KEY = "code";
    private static final String ERROR_KEY = "error";
    private static final String MESSAGE_KEY = "message";
    private static final String DETAILS_KEY = "details";

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        adaptResponseStatusOnInvalidRequestParametersError(webRequest);
        var errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        var error = getError(webRequest);
        log.debug("Enhance error attributes for error of type {}", error != null ? error.getClass().getCanonicalName() : "'null'");
        addRootExceptionCode(errorAttributes, error);
        enhanceInvalidRequestParametersError(errorAttributes, error);
        enhanceInvalidRequestError(errorAttributes, error);
        enhanceOpenAPIValidationError(errorAttributes, error);
        return errorAttributes;
    }

    private void adaptResponseStatusOnInvalidRequestParametersError(WebRequest webRequest) {
        var error = getError(webRequest);
        if (error != null && ConstraintViolationException.class.isAssignableFrom(error.getClass())) {
            webRequest.setAttribute("javax.servlet.error.status_code", BAD_REQUEST.value(), SCOPE_REQUEST);
        }
    }

    private void addRootExceptionCode(Map<String, Object> errorAttributes, Throwable error) {
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
        } else if (InvalidRequestException.class.isAssignableFrom(error.getClass()) ||
                ConstraintViolationException.class.isAssignableFrom(error.getClass())) {
            exceptionCode = ExceptionCode.INVALID_REQUEST;
        }
        errorAttributes.put(CODE_KEY, exceptionCode.code());
        errorAttributes.put(ERROR_KEY, exceptionCode.description());
    }

    private void enhanceInvalidRequestParametersError(Map<String, Object> errorAttributes, Throwable error) {
        if (error != null && ConstraintViolationException.class.isAssignableFrom(error.getClass())) {
            var constraintViolationError = (ConstraintViolationException) error;
            errorAttributes.put(MESSAGE_KEY, "Validation error");
            errorAttributes.put(DETAILS_KEY, listConstraintViolations(constraintViolationError));
        }
    }

    private List<String> listConstraintViolations(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(v -> format("Invalid parameter %s: %s. Actual value is '%s'", parameterPath(v), v.getMessage(), v.getInvalidValue()))
                .collect(toList());
    }

    private String parameterPath(ConstraintViolation<?> violation) {
        for (var node : violation.getPropertyPath()) {
            if (ElementKind.PARAMETER.equals(node.getKind())) {
                return node.toString();
            }
        }
        return "";
    }

    private void enhanceInvalidRequestError(Map<String, Object> errorAttributes, Throwable error) {
        if (error != null && InvalidRestRequestException.class.isAssignableFrom(error.getClass())) {
            var invalidRequestError = (InvalidRestRequestException) error;
            errorAttributes.put(MESSAGE_KEY, "Validation error");
            errorAttributes.put(DETAILS_KEY, invalidRequestError.getErrorMessages());
        }
    }

    private void enhanceOpenAPIValidationError(Map<String, Object> errorAttributes, Throwable error) {
        if (error != null && InvalidRequestException.class.isAssignableFrom(error.getClass())) {
            var openApiError = (InvalidRequestException) error;
            errorAttributes.put(MESSAGE_KEY, "OpenAPI validation error");
            errorAttributes.put(DETAILS_KEY, openApiError.getValidationReport().getMessages().stream()
                    .map(ValidationReport.Message::getMessage).collect(toList()));
        }
    }

}
