package cn.mghio.beans.exception;

/**
 * @author mghio
 * @since 2020-11-05
 */
public class TypeMismatchException extends RuntimeException {

    private transient Object value;

    private Class<?> requiredType;

    public TypeMismatchException(Object value, Class<?> requiredType) {
        super("Failed to convert value:" + value + " to type " + requiredType);
        this.value = value;
        this.requiredType = requiredType;
    }

    public Object getValue() {
        return value;
    }

    public Class<?> getRequiredType() {
        return requiredType;
    }
}
