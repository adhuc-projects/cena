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

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

/**
 * A HAL resource, providing convenient methods to embed resources as defined in the HAL specification.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @see http://stateless.co/hal_specification.html
 * @since 0.1.0
 */
public class HalResource extends ResourceSupport {

    private final Map<String, Object> embedded = new HashMap<>();

    /**
     * Gets the embedded resources.
     *
     * @return the embedded resources.
     */
    @JsonInclude(Include.NON_EMPTY)
    @JsonProperty("_embedded")
    public Map<String, Object> getEmbeddedResources() {
        return embedded;
    }

    /**
     * Embeds a new resource, with specified relationship.
     *
     * @param relationship the relationship for the embedded resource.
     * @param resource     the embedded resource.
     * @return this.
     */
    @SuppressWarnings("unchecked")
    public <R extends HalResource> R embedResource(@NonNull String relationship, @NonNull Object resource) {
        embedded.put(relationship, resource);
        return (R) this;
    }

    /**
     * Adds the specified link to the resource links.
     *
     * @param link the link to add.
     * @return this.
     */
    @SuppressWarnings("unchecked")
    public <R extends HalResource> R withLink(@NonNull Link link) {
        add(link);
        return (R) this;
    }

}
