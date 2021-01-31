package cn.mghio.beans.factory.config;

import cn.mghio.beans.exception.BeansException;

/**
 * @author mghio
 * @since 2021-01-31
 */
public interface BeanPostProcessor {

  Object beforeInitialization(Object bean, String beanName) throws BeansException;

  Object afterInitialization(Object bean, String beanName) throws BeansException;

}
