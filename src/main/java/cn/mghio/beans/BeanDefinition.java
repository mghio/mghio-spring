package cn.mghio.beans;

import java.util.List;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanDefinition {

    String getId();

    void setId(String id);

    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    BeanScope getScope();

    void setScope(String scope);

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    boolean hasConstructorArguments();

    Class<?> getBeanClass();

    void setBeanClass(Class<?> beanClass);

    void setBeanClassName(String beanClassName);

    Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;

    boolean hasBeanClass();
}
