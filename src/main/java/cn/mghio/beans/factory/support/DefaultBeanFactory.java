package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.BeanFactoryAware;
import cn.mghio.beans.factory.NoSuchBeanDefinitionException;
import cn.mghio.beans.factory.config.BeanPostProcessor;
import cn.mghio.beans.factory.config.DependencyDescriptor;
import cn.mghio.beans.factory.config.InstantiationAwareBeanProcessor;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.BeanDefinitionValueResolver;
import cn.mghio.beans.support.ConstructorResolver;
import cn.mghio.utils.ClassUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {

    private ClassLoader classLoader = null;

    // <beanId, BeanDefinition>
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

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
                bean = this.createBean(bd);
                this.registerSingleton(beanId, bean);
            }
            return bean;
        }
        return this.createBean(bd);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(name);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<>();
        List<String> beanIds = this.getBeanIdsByType(type);
        for (String beanId : beanIds) {
            result.add(this.getBean(beanId));
        }
        return result;
    }

    private List<String> getBeanIdsByType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanName))) {
                result.add(beanName);
            }
        }
        return result;
    }

    @Override
    public Object createBean(BeanDefinition bd) throws BeanCreationException {
        // 1. instantiate bean
        Object bean = instantiateBean(bd);
        // 2. populate bean
        populateBean(bd, bean);
        // 3. initialize bean
        bean = initializeBean(bd, bean);
        return bean;
    }

    private void populateBean(BeanDefinition bd, Object bean) {
        for (BeanPostProcessor postProcessor : this.getBeanPostProcessors()) {
            if (postProcessor instanceof InstantiationAwareBeanProcessor) {
                ((InstantiationAwareBeanProcessor) postProcessor).postProcessPropertyValues(bean, bd.getId());
            }
        }

        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();
                Object resolvedValue = resolver.resolveValueIfNecessary(originalValue);

                // 1. use java beans
                /*SimpleTypeConverted converter = new SimpleTypeConverted();
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
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

    @Override
    public void addBeanPostProcessor(BeanPostProcessor processor) {
        this.beanPostProcessors.add(processor);
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for (BeanDefinition bd : this.beanDefinitionMap.values()) {
            // note: make sure beanClass field has value
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return this.getBean(bd.getId());
            }
        }

        return null;
    }

    protected Object initializeBean(BeanDefinition bd, Object bean) {
        invokeAwareMethods(bean);

        // TODO(mghio): 调用 bean 的 init 方法，目前暂不实现

        // 创建代理
        if (!bd.isSynthetic()) {
            return applyBeanPostProcessorAfterInitialization(bean, bd.getId());
        }
        return bean;
    }

    private Object applyBeanPostProcessorAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor postProcessor : getBeanPostProcessors()) {
            result = postProcessor.afterInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    private void invokeAwareMethods(Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    private void resolveBeanClass(BeanDefinition bd) {
        if (bd.hasBeanClass()) {
            return;
        }

        try {
            bd.resolveBeanClass(this.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("can't load class: " + bd.getBeanClassName());
        }
    }
}
