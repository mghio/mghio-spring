package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.SimpleTypeConverted;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.BeanDefinitionValueResolver;
import cn.mghio.beans.support.ConstructorResolver;
import cn.mghio.utils.ClassUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class DefaultBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory,
        BeanDefinitionRegistry {

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
        // 1. instantiate bean
        Object bean = instantiateBean(bd);
        // 2. populate bean
        populateBean(bd, bean);
        return bean;
    }

    private void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverted converter = new SimpleTypeConverted();
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();
                Object resolvedValue = resolver.resolveValueIfNecessary(originalValue);

                // 1. use java beans
                /*BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(propertyName)) {
                        Object convertedValue = converter.convertIfNecessary(resolvedValue,
                                propertyDescriptor.getPropertyType());
                        propertyDescriptor.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }*/

                // 2. use BeanUtils
                BeanUtils.copyProperty(bean, propertyName, resolvedValue);
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]");
        }
    }

    private Object instantiateBean(BeanDefinition bd) {
        if (bd.hasConstructorArguments()) {
            ConstructorResolver constructorResolver = new ConstructorResolver(this);
            return constructorResolver.autowireConstructor(bd);
        } else {
            ClassLoader classLoader = this.getClassLoader();
            String beanClassName = bd.getBeanClassName();
            try {
                Class<?> beanClass = null;
                Class<?> cacheBeanClass = bd.getBeanClass();
                if (cacheBeanClass == null) {
                    beanClass = classLoader.loadClass(beanClassName);
                    bd.setBeanClass(beanClass);
                } else {
                    beanClass = cacheBeanClass;
                }
                return beanClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("Created bean for " + beanClassName + " fail.", e);
            }
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
