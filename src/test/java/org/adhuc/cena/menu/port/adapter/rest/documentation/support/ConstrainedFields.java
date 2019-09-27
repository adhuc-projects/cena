package org.adhuc.cena.menu.port.adapter.rest.documentation.support;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.util.StringUtils;

/**
 * A supporting class to document constrained fields.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @see ConstraintDescriptions
 * @since 0.1.0
 */
public class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    /**
     * Creates constrained fields documentation based on the specified constrained class.
     *
     * @param input the constrained class.
     */
    public ConstrainedFields(Class<?> input) {
        constraintDescriptions = new ConstraintDescriptions(input);
    }

    /**
     * Gets a field descriptor corresponding to the specified path in the constrained class.
     *
     * @param path the field path.
     * @return a field descriptor with documented constraints.
     */
    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path).attributes(key("constraints").value(
                StringUtils.collectionToDelimitedString(constraintDescriptions.descriptionsForProperty(path), ". ")));
    }

}
