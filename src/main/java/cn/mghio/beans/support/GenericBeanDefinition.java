package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.BeanScope;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;

    private String beanClassName;

    private BeanScope scope = BeanScope.DEFAULT;

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassNam() {
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

}
