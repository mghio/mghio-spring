package cn.mghio.aop.framework;

import cn.mghio.aop.Advice;
import cn.mghio.aop.Pointcut;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AdvisedSupport implements Advised {

  private boolean proxyTargetClass = false;
  private Object targetObject = null;
  private final List<Advice> advices = new ArrayList<>();
  private final List<Class<?>> interfaces = new ArrayList<>();

  public AdvisedSupport() {
  }

  @Override
  public Class<?> getTargetClass() {
    return this.targetObject.getClass();
  }

  @Override
  public boolean isInterfaceProxied(Class<?> intf) {
    return interfaces.contains(intf);
  }

  @Override
  public List<Advice> getAdvices() {
    return this.advices;
  }

  @Override
  public void addAdvice(Advice advice) {
    this.advices.add(advice);
  }

  @Override
  public List<Advice> getAdvices(Method method) {
    List<Advice> result = new ArrayList<>();
    for (Advice advice : this.getAdvices()) {
      Pointcut pc = advice.getPointcut();
      if (pc.getMethodMatcher().matches(method)) {
        result.add(advice);
      }
    }
    return result;
  }

  @Override
  public Object getTargetObject() {
    return this.targetObject;
  }

  @Override
  public boolean isProxyTargetClass() {
    return this.proxyTargetClass;
  }

  @Override
  public Class<?>[] getProxiedInterfaces() {
    return this.interfaces.toArray(new Class[0]);
  }

  @Override
  public void setTargetObject(Object targetObject) {
    this.proxyTargetClass = true;
    this.targetObject = targetObject;
  }

}
