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
package org.adhuc.cena.menu.port.adapter.rest;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The general API documentation.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
@Tag("integration")
@Tag("documentation")
@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiIndexController.class)
@ContextConfiguration(classes = ResultHandlerConfiguration.class)
@AutoConfigureRestDocs("build/generated-snippets")
@DisplayName("API documentation")
class ApiDocumentation {

    private static final String API_URL = "/api";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RestDocumentationResultHandler documentationHandler;

    @Test
    @DisplayName("generates headers example")
    void headersExample() throws Exception {
        mvc.perform(get(API_URL)).andExpect(status().isOk())
                .andDo(documentationHandler.document(responseHeaders(headerWithName("Content-Type")
                        .description("The Content-Type of the payload, e.g. `application/hal+json`"))));
    }

    @Test
    @DisplayName("generates index example")
    void indexExample() throws Exception {
        mvc.perform(get(API_URL)).andExpect(status().isOk()).andDo(documentationHandler.document(links(
                linkWithRel("documentation").description("This documentation"),
                linkWithRel("management").description("The https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints[Spring Boot Actuator] endpoints")),
                responseFields(subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));
    }

}
