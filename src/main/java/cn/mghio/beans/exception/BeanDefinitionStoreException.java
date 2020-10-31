package cn.mghio.beans.exception;

/**
 * Exception when there is an error reading the XML file.
 *
 * @author mghio
 * @since 2020-10-31
 */
public class BeanDefinitionStoreException extends BeansException {

    public BeanDefinitionStoreException(String message) {
        super(message);
    }

    public BeanDefinitionStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
