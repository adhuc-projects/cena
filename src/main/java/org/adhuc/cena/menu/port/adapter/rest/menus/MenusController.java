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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.adhuc.cena.menu.menus.DateRange.builder;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.validation.*;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.menus.ListMenus;
import org.adhuc.cena.menu.menus.MenuAppService;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.port.adapter.rest.support.Date;

/**
 * A REST controller exposing /api/menus resource.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@ExposesResourceFor(MenuModel.class)
@RequestMapping(path = "/api/menus", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class MenusController {

    private static final String SINCE_PARAM = "filter[date][since]";
    private static final String UNTIL_PARAM = "filter[date][until]";
    private static final int SINCE_PARAM_INDEX = 0;
    private static final int UNTIL_PARAM_INDEX = 1;

    private final EntityLinks links;
    private final MenuAppService menuAppService;
    private final MenuModelAssembler modelAssembler;

    /**
     * Gets the menu information for all menus for the menu owner corresponding to the authenticated user.
     */
    @GetMapping
    @ResponseStatus(OK)
    @DateRange
    CollectionModel<MenuModel> getMenus(@RequestParam(name = SINCE_PARAM, required = false) @Date(propertyName = SINCE_PARAM) String since,
                                        @RequestParam(name = UNTIL_PARAM, required = false) @Date(propertyName = UNTIL_PARAM) String until,
                                        Principal principal) {
        var menus = menuAppService.getMenus(new ListMenus(new MenuOwner(principal.getName()), parseDateRange(since, until)));
        return modelAssembler.toCollectionModel(menus);
    }

    /**
     * Creates a menu.
     */
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    ResponseEntity<Void> createMenu(@RequestBody @Valid CreateMenuRequest request, Errors errors, Principal principal) throws URISyntaxException {
        menuAppService.createMenu(request.toCommand(principal.getName()));
        return ResponseEntity.created(new URI(links.linkToItemResource(MenuModel.class,
                String.format("%s-%s", request.getDate(), request.getMealType())).getHref())).build();
    }

    private static org.adhuc.cena.menu.menus.DateRange parseDateRange(String since, String until) {
        return parseDateRange(
                since != null ? LocalDate.parse(since) : null,
                until != null ? LocalDate.parse(until) : null);
    }

    private static org.adhuc.cena.menu.menus.DateRange parseDateRange(LocalDate since, LocalDate until) {
        var builder = builder();
        if (since != null) builder = builder.withSince(since);
        if (until != null) builder = builder.withUntil(until);
        return builder.build();
    }

    @Documented
    @Target({METHOD})
    @Retention(RUNTIME)
    @Constraint(validatedBy = DateRange.DateRangeConstraintValidator.class)
    @interface DateRange {

        String message() default "{menus.ListMenus.DateRange.message}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        @SupportedValidationTarget(ValidationTarget.PARAMETERS)
        class DateRangeConstraintValidator implements ConstraintValidator<DateRange, Object[]> {
            @Override
            public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {
                var since = parseDate(parameters[SINCE_PARAM_INDEX]);
                var until = parseDate(parameters[UNTIL_PARAM_INDEX]);
                try {
                    parseDateRange(since, until);
                    return true;
                } catch (IllegalArgumentException e) {
                    return false;
                }
            }

            private LocalDate parseDate(Object parameter) {
                try {
                    return LocalDate.parse((String) parameter);
                } catch (NullPointerException | DateTimeParseException e) {
                    return null;
                }
            }
        }

    }

}
