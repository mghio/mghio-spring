package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    private ClassLoader classLoader = null;

    // <beanId, BeanDefinition>
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);

    public DefaultBeanFactory() {

    }

    @Override
    public Object getBean(String beanId) {
        BeanDefinition bd = getBeanDefinition(beanId);
        if (null == bd) {
            return null;
        }
        if (bd.isSingleton()) {
            Object bean = this.getSingleton(beanId);
            if (null == bean) {
                bean = this.doCreateBean(bd);
                this.registerSingleton(beanId, bean);
            }
            return bean;
        }
        return this.doCreateBean(bd);
    }

    private Object doCreateBean(BeanDefinition bd) {
        ClassLoader classLoader = this.getClassLoader();
        String beanClassName = bd.getBeanClassNam();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new BeanCreationException("Created bean for " + beanClassName + " fail.", e);
        }
    }

    @Override
    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanId, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return beanDefinitionMap.get(beanId);
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return (null != classLoader) ? classLoader : ClassUtils.getDefaultClassLoader();
    }
}
