package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.BeanScope;
import cn.mghio.beans.ConstructorArgument;
import cn.mghio.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;

    private String beanClassName;

    private BeanScope scope = BeanScope.DEFAULT;

    private Class<?> beanClass;

    private final List<PropertyValue> propertyValues = new ArrayList<>();

    private final ConstructorArgument constructorArgument = new ConstructorArgument();

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getId() {
        return this.beanId;
    }

    @Override
    public void setId(String id) {
        this.beanId = id;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.scope.isSingleton();
    }

    @Override
    public boolean isPrototype() {
        return this.scope.isPrototype();
    }

    @Override
    public BeanScope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = BeanScope.of(scope);
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    @Override
    public boolean hasConstructorArguments() {
        return !this.constructorArgument.isEmpty();
    }

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = this.getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }
}
