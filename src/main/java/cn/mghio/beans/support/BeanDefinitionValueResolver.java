package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.FactoryBean;
import cn.mghio.beans.factory.support.AbstractBeanFactory;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class BeanDefinitionValueResolver {

    private final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            return this.beanFactory.getBean(refName);
        } else if (value instanceof TypedStringValue) {
            TypedStringValue stringValue = (TypedStringValue) value;
            return stringValue.getValue();
        } else if (value instanceof BeanDefinition) {
            // Resolve plain BeanDefinition, without contained name: use dummy name.
            BeanDefinition bd = (BeanDefinition) value;
            String innerBeanName = "(inner bean)" + bd.getBeanClassName() + "#" +
                Integer.toHexString(System.identityHashCode(bd));
            return resolveInnerBean(innerBeanName, bd);
        } else {
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition bd) {
        try {
            Object innerBean = this.beanFactory.createBean(bd);
            if (innerBean instanceof FactoryBean) {
                return ((FactoryBean<?>) innerBean).getObject();
            } else {
                return innerBean;
            }
        } catch (BeanCreationException e) {
            throw new BeanCreationException(innerBeanName, "Cannot create inner bean '" + innerBeanName + "'");
        } catch (Exception e) {
            throw new BeanCreationException(e.getMessage());
        }
    }
}
