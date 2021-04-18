package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;

/**
 * @author mghio
 * @since 2021-04-18
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
    implements ConfigurableBeanFactory {

  public abstract Object createBean(BeanDefinition bd) throws BeanCreationException;

}
