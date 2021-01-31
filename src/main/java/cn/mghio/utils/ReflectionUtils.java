package cn.mghio.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class ReflectionUtils {

  /**
   * Cache for {@link Class#getDeclaredMethods()}, allowing for fast resolution.
   */
  private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentHashMap<>(256);

  public static void makeAccessible(Field field) {
    if (!Modifier.isPublic(field.getModifiers()) ||
        !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
        Modifier.isFinal(field.getModifiers()) &&
            !field.isAccessible()) {
      field.setAccessible(true);
    }
  }

  public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
    Assert.notNull(clazz, "Class must not be null");
    Assert.notNull(name, "Method name must not be null");
    Class<?> searchType = clazz;
    while (searchType != null) {
      Method[] methods = (searchType.isInterface() ? searchType.getMethods()
          : getDeclaredMethods(searchType));
      for (Method method : methods) {
        if (name.equals(method.getName()) &&
            (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
          return method;
        }
      }
      searchType = searchType.getSuperclass();
    }
    return null;
  }

  public static Object invokeMethod(Method method, Object target) {
    return invokeMethod(method, target, new Object[0]);
  }

  /**
   * Invoke the specified {@link Method} against the supplied target object with the
   * supplied arguments. The target object can be {@code null} when invoking a
   * static {@link Method}.
   * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException}.
   * @param method the method to invoke
   * @param target the target object to invoke the method on
   * @param args the invocation arguments (may be {@code null})
   * @return the invocation result, if any
   */
  public static Object invokeMethod(Method method, Object target, Object... args) {
    try {
      return method.invoke(target, args);
    }
    catch (Exception ex) {
      handleReflectionException(ex);
    }
    throw new IllegalStateException("Should never get here");
  }

  public static void handleReflectionException(Exception ex) {
    if (ex instanceof NoSuchMethodException) {
      throw new IllegalStateException("Method not found: " + ex.getMessage());
    }
    if (ex instanceof IllegalAccessException) {
      throw new IllegalStateException("Could not access method: " + ex.getMessage());
    }
    if (ex instanceof InvocationTargetException) {
      handleInvocationTargetException((InvocationTargetException) ex);
    }
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  public static void handleInvocationTargetException(InvocationTargetException ex) {
    rethrowRuntimeException(ex.getTargetException());
  }

  public static void rethrowRuntimeException(Throwable ex) {
    if (ex instanceof RuntimeException) {
      throw (RuntimeException) ex;
    }
    if (ex instanceof Error) {
      throw (Error) ex;
    }
    throw new UndeclaredThrowableException(ex);
  }

  private static Method[] getDeclaredMethods(Class<?> clazz) {
    return declaredMethodsCache.computeIfAbsent(clazz, k -> clazz.getDeclaredMethods());
  }

}
