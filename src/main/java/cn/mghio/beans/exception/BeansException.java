package cn.mghio.beans.exception;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class BeansException extends RuntimeException {

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
