package cn.mghio.aop.framework;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

  protected final Object targetObject;
  protected final Method targetMethod;
  protected Object[] arguments;
  protected final List<MethodInterceptor> interceptors;
  private int currentInterceptorIndex = -1;

  public ReflectiveMethodInvocation(Object targetObject, Method targetMethod,
      Object[] arguments, List<MethodInterceptor> interceptors) {
    this.targetObject = targetObject;
    this.targetMethod = targetMethod;
    this.arguments = arguments;
    this.interceptors = interceptors;
  }

  @Override
  public Object proceed() throws Throwable {
    // 所有的拦截器已经调用完成
    if (this.currentInterceptorIndex == interceptors.size() - 1) {
      return invokeJoinpoint();
    }

    this.currentInterceptorIndex++;
    MethodInterceptor methodInterceptor = this.interceptors.get(this.currentInterceptorIndex);
    return methodInterceptor.invoke(this);
  }

  @Override
  public Method getMethod() {
    return this.targetMethod;
  }

  @Override
  public Object[] getArguments() {
    return this.arguments;
  }

  @Override
  public Object getThis() {
    return this.targetObject;
  }

  @Override
  public AccessibleObject getStaticPart() {
    return this.targetMethod;
  }

  private Object invokeJoinpoint() throws Throwable {
    return this.targetMethod.invoke(this.targetObject, this.arguments);
  }

}
