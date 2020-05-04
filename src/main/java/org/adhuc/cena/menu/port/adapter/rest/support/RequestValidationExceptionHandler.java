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
package org.adhuc.cena.menu.port.adapter.rest.support;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import static org.adhuc.cena.menu.common.ExceptionCode.INVALID_REQUEST;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;

import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A {@link RestControllerAdvice} that handles {@link ConstraintViolationException}s to retrieve list of errors from the
 * exception and build the response body, returning a {@link HttpStatus#BAD_REQUEST} status.
 * <p>
 * This implementation can extract a custom property name from validation annotations, using an extra {@code propertyName}
 * attribute on those annotations and setting the property name on it. It is especially useful when the annotated parameter
 * name in the controller method has not the same name as the one used by the query parameter or request body property.
 * Example:
 * <pre>
 * CollectionModel<RecipeModel> getRecipes(@RequestParam(name = "filter[ingredient]", required = false) @Uuid(propertyName = "filter[ingredient]") String ingredient) {
 *     ...
 * }
 * </pre>
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@RestControllerAdvice
@RequiredArgsConstructor
class RequestValidationExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String PROPERTY_NAME_CONSTRAINT_ATTRIBUTE_KEY = "propertyName";

    @ExceptionHandler(InvalidRequestException.class)
    ResponseEntity<Object> handleConstraintViolation(InvalidRequestException e, WebRequest request) {
        var error = new Error(BAD_REQUEST, INVALID_REQUEST)
                .withMessage("OpenAPI validation error")
                .withDetails(e.getValidationReport().getMessages().stream()
                        .map(ValidationReport.Message::getMessage).collect(toList()));
        return handleExceptionInternal(e, error, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException e, WebRequest request) {
        var error = new Error(BAD_REQUEST, INVALID_REQUEST)
                .withMessage("Validation error")
                .withDetails(listConstraintViolations(e));
        return handleExceptionInternal(e, error, new HttpHeaders(), BAD_REQUEST, request);
    }

    private List<String> listConstraintViolations(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(this::toViolatedElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ViolatedElement::toString)
                .collect(toList());
    }

    private Optional<ViolatedElement> toViolatedElement(ConstraintViolation<?> violation) {
        var propertyName = extractPropertyName(violation);
        ViolatedElement violatedElement = null;
        for (var node : violation.getPropertyPath()) {
            violatedElement = ViolatedElementType.fromElementKind(node.getKind())
                    .map(type -> new ViolatedElement(type, propertyName.orElse(node.getName()), violation.getMessage(), violation.getInvalidValue()))
                    .orElse(null);
        }
        return Optional.ofNullable(violatedElement);
    }

    private Optional<String> extractPropertyName(ConstraintViolation<?> violation) {
        var value = violation.getConstraintDescriptor().getAttributes().get(PROPERTY_NAME_CONSTRAINT_ATTRIBUTE_KEY);
        return (value != null && String.class.isAssignableFrom(value.getClass())) ? Optional.of((String) value) : empty();
    }

    @Value
    private static class ViolatedElement {

        static final Function<ViolatedElement, String> WITH_NAME_AND_VALUE =
                v -> String.format("Invalid %s '%s': %s. Actual value is '%s'", v.type, v.name, v.violation, v.value);
        static final Function<ViolatedElement, String> WITH_NAME_AND_NULL_VALUE =
                v -> String.format("Invalid %s '%s': %s. Actual value is <null>", v.type, v.name, v.violation);
        static final Function<ViolatedElement, String> REQUEST_VIOLATION =
                v -> String.format("Invalid %s: %s", v.type, v.violation);

        @NonNull
        private ViolatedElementType type;
        // Name can be null in case of REQUEST_BODY type
        private String name;
        @NonNull
        private String violation;
        private Object value;

        public String toString() {
            var formatter = value != null ? type.formatter : type.nullValueFormatter;
            return formatter.apply(this);
        }
    }

    @AllArgsConstructor
    private enum ViolatedElementType {
        REQUEST_PROPERTY(ElementKind.PROPERTY, "request body property", ViolatedElement.WITH_NAME_AND_VALUE, ViolatedElement.WITH_NAME_AND_NULL_VALUE),
        REQUEST_BODY(ElementKind.BEAN, "request", ViolatedElement.REQUEST_VIOLATION, ViolatedElement.REQUEST_VIOLATION),
        QUERY_PARAMETER(ElementKind.PARAMETER, "query parameter", ViolatedElement.WITH_NAME_AND_VALUE, ViolatedElement.WITH_NAME_AND_NULL_VALUE),
        CROSS_PARAMETER(ElementKind.CROSS_PARAMETER, "query parameters", ViolatedElement.REQUEST_VIOLATION, ViolatedElement.REQUEST_VIOLATION);

        private ElementKind kind;
        private String name;
        private Function<ViolatedElement, String> formatter;
        private Function<ViolatedElement, String> nullValueFormatter;

        static Optional<ViolatedElementType> fromElementKind(ElementKind kind) {
            return Arrays.stream(ViolatedElementType.values()).filter(type -> type.kind.equals(kind)).findFirst();
        }

        public String toString() {
            return name;
        }
    }

}
