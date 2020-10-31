package cn.mghio.beans.exception;

/**
 * Exception when creating a bean error.
 *
 * @author mghio
 * @since 2020-10-31
 */
public class BeanCreationException extends BeansException {

    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
