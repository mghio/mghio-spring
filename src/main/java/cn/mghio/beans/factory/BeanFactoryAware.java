package cn.mghio.beans.factory;

import cn.mghio.beans.exception.BeansException;

/**
 * @author mghio
 * @since 2021-04-18
 */
public interface BeanFactoryAware {

  void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
