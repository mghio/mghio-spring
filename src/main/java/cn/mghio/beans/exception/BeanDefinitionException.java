package cn.mghio.beans.exception;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class BeanDefinitionException extends BeansException {

    public BeanDefinitionException(String message) {
        super(message);
    }

    public BeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
