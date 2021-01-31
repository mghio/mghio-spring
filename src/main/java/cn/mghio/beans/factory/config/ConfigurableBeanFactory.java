package cn.mghio.beans.factory.config;

import java.util.List;

/**
 * @author mghio
 * @since 2020-11-01
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();

    void addBeanPostProcessor(BeanPostProcessor processor);

    List<BeanPostProcessor> getBeanPostProcessors();
}
