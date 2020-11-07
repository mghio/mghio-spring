package cn.mghio.beans.support;

import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.factory.support.DefaultBeanFactory;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class BeanDefinitionResolver {

    private final DefaultBeanFactory factory;

    public BeanDefinitionResolver(DefaultBeanFactory factory) {
        this.factory = factory;
    }

    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = this.factory.getBean(refName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            TypedStringValue stringValue = (TypedStringValue) value;
            return stringValue.getValue();
        } else {
            throw new RuntimeException("The value " + value + " has not implemented");
        }
    }
}
