package cn.mghio.beans;

import cn.mghio.beans.exception.TypeMismatchException;

/**
 * @author mghio
 * @since 2020-11-05
 */
public interface TypeConverted {

    <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;

}
