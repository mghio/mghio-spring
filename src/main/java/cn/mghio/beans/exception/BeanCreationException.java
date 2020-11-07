package cn.mghio.beans.exception;

/**
 * Exception when creating a bean error.
 *
 * @author mghio
 * @since 2020-10-31
 */
public class BeanCreationException extends BeansException {

    private String beanName;

    public BeanCreationException(String message) {
        super(message);
    }

    public BeanCreationException(String message, String beanName) {
        super("Error creating bean with name '" + beanName + "':" + message);
        this.beanName = beanName;
    }

    public BeanCreationException(String message, String beanName, Throwable cause) {
        super("Error creating bean with name '" + beanName + "':" + message, cause);
        this.beanName = beanName;
    }

    public BeanCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
