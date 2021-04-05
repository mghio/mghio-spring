package cn.mghio.aop;

/**
 * @author mghio
 * @since 2021-04-04
 */
public interface Pointcut {

  MethodMatcher getMethodMatcher();

  String getExpression();

}
