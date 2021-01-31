package cn.mghio.beans.factory.config;

import cn.mghio.beans.exception.BeansException;

/**
 * @author mghio
 * @since 2021-01-31
 */
public interface InstantiationAwareBeanProcessor extends BeanPostProcessor {

  Object beforeInstantiation(Class<?> beanClz, String beanName) throws BeansException;

  boolean afterInstantiation(Object bean, String beanName) throws BeansException;

  void postProcessPropertyValues(Object bean, String beanName) throws BeansException;

}
