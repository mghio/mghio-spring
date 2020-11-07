package cn.mghio.beans;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class RuntimeBeanReference {

    private final String beanName;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
