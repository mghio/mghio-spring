package cn.mghio.beans.factory.config;

import cn.mghio.beans.factory.BeanFactory;

/**
 * @author mghio
 * @since 2020-11-01
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    void setClassLoader(ClassLoader classLoader);

    ClassLoader getClassLoader();

}
