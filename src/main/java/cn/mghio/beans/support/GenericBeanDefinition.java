package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.BeanScope;
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


    private List<PropertyValue> propertyValues = new ArrayList<>();

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
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

}
