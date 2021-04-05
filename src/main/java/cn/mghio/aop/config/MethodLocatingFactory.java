package cn.mghio.aop.config;

import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.beans.BeanUtils;
import cn.mghio.utils.StringUtils;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class MethodLocatingFactory {

  private String targetBeanName;

  private String methodName;

  private Method method;

  public void setTargetBeanName(String targetBeanName) {
    this.targetBeanName = targetBeanName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setBeanFactory(BeanFactory beanFactory) {
    if (!StringUtils.hasText(this.targetBeanName)) {
      throw new IllegalArgumentException("Property 'targetBeanName' is required");
    }
    if (!StringUtils.hasText(this.methodName)) {
      throw new IllegalArgumentException("Property 'methodName' is required");
    }

    Class<?> beanClass = beanFactory.getType(this.targetBeanName);
    if (Objects.isNull(beanClass)) {
      throw new IllegalArgumentException("Can't determine type of bean with name '" + this.targetBeanName);
    }

    this.method = BeanUtils.resolveSignature(this.methodName, beanClass);
    if (Objects.isNull(this.method)) {
      throw new IllegalArgumentException("Unable to locate method [" + this.methodName + "] on bean ["
          + this.targetBeanName + "]");
    }
  }

  public Method getObject() {
    return this.method;
  }
}
