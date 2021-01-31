package cn.mghio.beans.factory.config;

import cn.mghio.beans.factory.BeanFactory;

/**
 * @author mghio
 * @since 2021-01-30
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

  Object resolveDependency(DependencyDescriptor descriptor);

}
