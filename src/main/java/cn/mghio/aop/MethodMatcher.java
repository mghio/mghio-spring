package cn.mghio.aop;

import java.lang.reflect.Method;

/**
 * @author mghio
 * @since 2021-04-04
 */
public interface MethodMatcher {

  boolean matches(Method method);

}
