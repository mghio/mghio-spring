package cn.mghio.aop.framework;

import cn.mghio.aop.Advice;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author mghio
 * @since 2021-04-05
 */
public interface Advised {

  Class<?> getTargetClass();

  boolean isInterfaceProxied(Class<?> intf);

  List<Advice> getAdvices();

  void addAdvice(Advice advice);

  List<Advice> getAdvices(Method method);

  /*------------------- JDK Proxy ------------------*/

  Object getTargetObject();

  void setTargetObject(Object targetObject);

  boolean isProxyTargetClass();

  Class<?>[] getProxiedInterfaces();
}
