package cn.mghio.aop.aspectj;

import cn.mghio.aop.Advice;
import cn.mghio.aop.MethodMatcher;
import cn.mghio.aop.Pointcut;
import cn.mghio.aop.framework.Advised;
import cn.mghio.aop.framework.AdvisedSupport;
import cn.mghio.aop.framework.AopProxyFactory;
import cn.mghio.aop.framework.CglibProxyFactory;
import cn.mghio.beans.exception.BeansException;
import cn.mghio.beans.factory.config.BeanPostProcessor;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.utils.ClassUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class AspectJAutoProxyCreator implements BeanPostProcessor {

  private ConfigurableBeanFactory beanFactory;

  @Override
  public Object beforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object afterInitialization(Object bean, String beanName) throws BeansException {
    // 如果这个 bean 本身就是 Advice 及其子类，则不生成动态代理
    if (isInfrastructureClass(bean.getClass())) {
      return bean;
    }

    List<Advice> advices = getCandidateAdvices(bean);
    if (advices.isEmpty()) {
      return bean;
    }

    return createProxy(advices, bean);
  }

  protected Object createProxy(List<Advice> advices, Object bean) {
    Advised config = new AdvisedSupport();
    for (Advice advice : advices) {
      config.addAdvice(advice);
    }

    Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(bean.getClass());
    for (Class targetInterface : targetInterfaces) {
      config.addInterface(targetInterface);
    }
    config.setTargetObject(bean);

    AopProxyFactory proxyFactory = null;
    if (config.getProxiedInterfaces().length == 0) {
      // CGLIB 代理
      proxyFactory = new CglibProxyFactory(config);
    } else {
      // TODO: JDK 动态代理

    }

    return proxyFactory.getProxy();
  }

  public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  private List<Advice> getCandidateAdvices(Object bean) {
    List<Object> advices = this.beanFactory.getBeansByType(Advice.class);
    List<Advice> result = new ArrayList<>();
    for (Object advice : advices) {
      Pointcut pointcut = ((Advice) advice).getPointcut();
      if (canApply(pointcut, bean.getClass())) {
        result.add((Advice) advice);
      }
    }
    return result;
  }

  private boolean canApply(Pointcut pointcut, Class<?> targetClass) {
    MethodMatcher methodMatcher = pointcut.getMethodMatcher();
    Set<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
    classes.add(targetClass);
    for (Class<?> clazz : classes) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method m : methods) {
        if (methodMatcher.matches(m)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isInfrastructureClass(Class<?> beanClass) {
    return Advice.class.isAssignableFrom(beanClass);
  }
}
