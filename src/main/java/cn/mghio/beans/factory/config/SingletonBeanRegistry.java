package cn.mghio.beans.factory.config;

/**
 * @author mghio
 * @since 2020-11-01
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

}
