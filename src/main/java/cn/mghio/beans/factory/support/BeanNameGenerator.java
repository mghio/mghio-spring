package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.support.BeanDefinitionRegistry;

/**
 * @author mghio
 * @since 2020-11-28
 */
public interface BeanNameGenerator {

    String generateBeanName(BeanDefinition bd, BeanDefinitionRegistry registry);

}
