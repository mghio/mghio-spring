package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;

/**
 * @author mghio
 * @since 2020-11-01
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanId, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanId);

}
