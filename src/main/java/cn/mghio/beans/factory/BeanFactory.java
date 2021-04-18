package cn.mghio.beans.factory;

import java.util.List;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanFactory {

    Object getBean(String beanId);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);
}
