package cn.mghio.aop.config;

import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.beans.factory.BeanFactoryAware;
import cn.mghio.utils.StringUtils;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class AopInstanceFactory implements BeanFactoryAware {

  private String aspectBeanName;

  private BeanFactory beanFactory;

  public void setAspectBeanName(String aspectBeanName) {
    this.aspectBeanName = aspectBeanName;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
    if (!StringUtils.hasText(this.aspectBeanName)) {
      throw new IllegalArgumentException("'aspectName' is required");
    }
  }

  public Object getAspectInstance() {
    return this.beanFactory.getBean(this.aspectBeanName);
  }
}
