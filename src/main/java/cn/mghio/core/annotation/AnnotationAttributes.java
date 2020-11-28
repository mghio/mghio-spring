package cn.mghio.core.annotation;

import cn.mghio.utils.Assert;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mghio
 * @since 2020-11-25
 */
@NoArgsConstructor
public class AnnotationAttributes extends LinkedHashMap<String, Object> {

    public AnnotationAttributes(int initialCapacity) {
        super(initialCapacity);
    }

    public AnnotationAttributes(Map<? extends String, ?> map) {
        super(map);
    }

    public String getString(String attributeName) {
        return doGet(attributeName, String.class);
    }

    public String[] getArray(String attributeName) {
        return doGet(attributeName, String[].class);
    }

    public Boolean getBoolean(String attributeName) {
        return doGet(attributeName, Boolean.class);
    }

    @SuppressWarnings("all")
    public <N extends Number> N getNumber(String attributeName) {
        return (N) doGet(attributeName, Number.class);
    }

    @SuppressWarnings("all")
    public <E extends Enum> E getEnum(String attributeName) {
        return (E) doGet(attributeName, Enum.class);
    }

    @SuppressWarnings("all")
    private <T> T doGet(String attributeName, Class<T> expectedType) {
        Assert.hasText(attributeName, "attributeName must not null or empty");
        Object value = this.get(attributeName);
        Assert.notNull(value, String.format("Attribute '%s' not found", attributeName));
        Assert.isAssignable(expectedType, value.getClass(), String.format("Attribute '%s' is of type [%s], but [%s] was expected. Cause: ",
                attributeName, value.getClass().getSimpleName(), expectedType.getSimpleName()));
        return (T) value;
    }
}
