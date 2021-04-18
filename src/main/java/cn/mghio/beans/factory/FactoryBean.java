package cn.mghio.beans.factory;

/**
 * @author mghio
 * @since 2021-04-18
 */
public interface FactoryBean<T> {

  T getObject() throws Exception;

  Class<?> getObjectType();
}
