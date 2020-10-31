package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;

    private String beanClassName;

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassNam() {
        return this.beanClassName;
    }

}
