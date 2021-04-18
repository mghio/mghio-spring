package cn.mghio.beans;

import static cn.mghio.utils.ReflectionUtils.findMethod;

import cn.mghio.utils.Assert;
import cn.mghio.utils.ClassUtils;
import cn.mghio.utils.StringUtils;
import java.lang.reflect.Method;

/**
 * @author mghio
 * @since 2021-04-05
 */
public abstract class BeanUtils {

  public static Method resolveSignature(String signature, Class<?> clazz) {
    Assert.hasText(signature, "'signature' must not be empty");
    Assert.notNull(clazz, "Class must not be null");

    int firstParen = signature.indexOf("(");
    int lastParen = signature.indexOf(")");
    if (firstParen > -1 && lastParen == -1) {
      throw new IllegalArgumentException("Invalid method signature '" + signature +
          "': expected closing ')' for args list");
    } else if (lastParen > -1 && firstParen == -1) {
      throw new IllegalArgumentException("Invalid method signature '" + signature +
          "': expected opening '(' for args list");
    } else if (firstParen == -1) {
      return findMethodWithMinimalParameters(clazz, signature);
    } else {
      String methodName = signature.substring(0, firstParen);
      String[] parameterTypeNames =
          StringUtils
              .commaDelimitedListToStringArray(signature.substring(firstParen + 1, lastParen));
      Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.length];
      for (int i = 0; i < parameterTypeNames.length; i++) {
        String parameterTypeName = parameterTypeNames[i].trim();
        try {
          parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
        } catch (Throwable ex) {
          throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" +
              parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
        }
      }
      return findMethod(clazz, methodName, parameterTypes);
    }
  }

  public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName)
      throws IllegalArgumentException {

    Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
    if (targetMethod == null && clazz.getSuperclass() != null) {
      targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
    }
    return targetMethod;
  }

  public static Method findMethodWithMinimalParameters(Method[] methods, String methodName)
      throws IllegalArgumentException {

    Method targetMethod = null;
    int numMethodsFoundWithCurrentMinimumArgs = 0;
    for (Method method : methods) {
      if (method.getName().equals(methodName)) {
        int numParams = method.getParameterTypes().length;
        if (targetMethod == null || numParams < targetMethod.getParameterTypes().length) {
          targetMethod = method;
          numMethodsFoundWithCurrentMinimumArgs = 1;
        } else {
          if (targetMethod.getParameterTypes().length == numParams) {
            // Additional candidate with same length
            numMethodsFoundWithCurrentMinimumArgs++;
          }
        }
      }
    }
    if (numMethodsFoundWithCurrentMinimumArgs > 1) {
      throw new IllegalArgumentException("Cannot resolve method '" + methodName +
          "' to a unique method. Attempted to resolve to overloaded method with " +
          "the least number of parameters, but there were " +
          numMethodsFoundWithCurrentMinimumArgs + " candidates.");
    }
    return targetMethod;
  }

  public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName)
      throws IllegalArgumentException {

    Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
    if (targetMethod == null) {
      targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
    }
    return targetMethod;
  }

}
