package cn.mghio.beans.factory;

import cn.mghio.beans.BeanDefinition;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanFactory {

    Object getBean(String beanId);

    void registerBeanDefinition(String beanId, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanId);

}
