package fr.pinguet62.jsfring.gui.util.querydsl.converter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Function;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.EntityPathBase;

/**
 * Get the {@link SimpleExpression} of {@link EntityPath} from the property
 * name.
 */
public final class PropertyConverter implements Function<String, SimpleExpression<?>> {

    /** The meta object. */
    private final EntityPathBase<?> meta;

    /**
     * Constructor.
     *
     * @param meta The meta-object.
     */
    public PropertyConverter(EntityPathBase<?> meta) {
        this.meta = meta;
    }

    /**
     * Apply the conversion: get the value corresponding to the attribute.
     * <p>
     * For example, from {@code meta} and {@code "attr.subAttr"}, the method
     * will return the value of attribute {@code meta.attr.subAttr} value.
     *
     * @param property The {@link String} property.
     * @return The {@link SimpleExpression}.
     * @throws IllegalArgumentException Invalid property value: invalid field
     *             name, space, invalid character, ...
     * @throws NullPointerException The property is {@code null}.
     * @throws ClassCastException The target field is not of type
     *             {@link SimpleExpression}.
     */
    @Override
    public SimpleExpression<?> apply(String property) {
        Objects.nonNull(property);
        if (property.isEmpty() || property.endsWith("."))
            throw new IllegalArgumentException("Invalid properties path: " + property);

        SimpleExpression<?> targetAttribute = meta;
        for (String attributeName : property.split("\\.")) {
            // Field
            Field field;
            try {
                field = targetAttribute.getClass().getDeclaredField(attributeName);
            } catch (NoSuchFieldException | SecurityException exception) {
                throw new IllegalArgumentException("Attribute not found: " + targetAttribute.getClass() + "#" + attributeName,
                        exception);
            }
            // Value
            try {
                targetAttribute = (SimpleExpression<?>) field.get(targetAttribute);
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                throw new RuntimeException("Error during getting the attribute value: " + targetAttribute.getClass() + "#"
                        + attributeName, exception);
            }
        }
        return targetAttribute;
    }

}
