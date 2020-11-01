package cn.mghio.beans.factory;

/**
 * @author mghio
 * @since 2020-10-31
 */
public interface BeanFactory {

    Object getBean(String beanId);

}
