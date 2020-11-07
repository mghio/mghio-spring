package cn.mghio.beans;

import java.util.List;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanDefinition {

    String getBeanClassName();

    boolean isSingleton();

    boolean isPrototype();

    BeanScope getScope();

    void setScope(String scope);

    List<PropertyValue> getPropertyValues();
}
