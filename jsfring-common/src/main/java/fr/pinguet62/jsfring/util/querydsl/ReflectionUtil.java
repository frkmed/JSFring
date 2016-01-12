package fr.pinguet62.jsfring.util.querydsl;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.persistence.Entity;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.EntityPathBase;

public final class ReflectionUtil {

    /**
     * Get the default meta-object of {@link Entity} class.
     * <p>
     * The meta-classes must be generated with default options:
     * <ul>
     * <li>The same package that the {@link Entity} type;</li>
     * <li>The name of meta-class must be have the default target name,
     * beginning by {@code Q}.<br>
     * For example if the name is {@code mypackage.MyClass}, the meta-class must
     * be {@code mypackage.QMyClass}.</li>
     * </ul>
     *
     * @param <T> The {@link Entity} type.
     * @param entityType The {@link Entity} type.
     * @return The {@link EntityPathBase} corresponding.
     * @throws UnsupportedOperationException Meta-class not found from
     *             {@link Entity}.
     * @throws UnsupportedOperationException Error during getting default
     *             meta-instance.
     */
    @SuppressWarnings("unchecked")
    public static <T> EntityPath<T> getDefaultMetaObject(Class<T> entityType) {
        // Target class
        String metaObjectTypeName = entityType.getName();
        metaObjectTypeName = new StringBuffer(metaObjectTypeName).insert(metaObjectTypeName.lastIndexOf('.') + 1, 'Q')
                .toString();
        Class<? extends EntityPathBase<T>> metaObjectType;
        try {
            metaObjectType = (Class<? extends EntityPathBase<T>>) Class.forName(metaObjectTypeName);
        } catch (ClassNotFoundException exception) {
            throw new UnsupportedOperationException("Metaclass not found", exception);
        }

        // Field
        List<Field> fields = asList(metaObjectType.getDeclaredFields()).stream()
                .filter(/* Static */attr -> Modifier.isStatic(attr.getModifiers()))
                .filter(/* Same type */attr -> attr.getType().equals(metaObjectType)).collect(toList());
        if (fields.isEmpty())
            throw new IllegalArgumentException("Field not found.");
        else if (fields.size() > 1)
            throw new IllegalArgumentException("More than 1 field found.");
        Field metaField = fields.get(0);

        try {
            return (EntityPathBase<T>) metaField.get(null);
        } catch (IllegalArgumentException | IllegalAccessException exception) {
            throw new UnsupportedOperationException("Error during getting default meta-instance", exception);
        }
    }

    /** Private constructor: not instantiable. */
    private ReflectionUtil() {}

}
