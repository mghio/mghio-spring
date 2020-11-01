package cn.mghio.beans;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanDefinition {

    String getBeanClassNam();

    boolean isSingleton();

    boolean isPrototype();

    BeanScope getScope();

    void setScope(String scope);
}
