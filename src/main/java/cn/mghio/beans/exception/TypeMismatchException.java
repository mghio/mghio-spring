package cn.mghio.beans.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mghio
 * @since 2020-11-05
 */
@Getter
@AllArgsConstructor
public class TypeMismatchException extends RuntimeException {

    private transient Object value;

    private Class<?> requiredType;
}
