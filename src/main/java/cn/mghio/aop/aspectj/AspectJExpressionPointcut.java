package cn.mghio.aop.aspectj;

import cn.mghio.aop.MethodMatcher;
import cn.mghio.aop.Pointcut;
import cn.mghio.utils.ClassUtils;
import cn.mghio.utils.StringUtils;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

/**
 * @author mghio
 * @since 2021-04-04
 */
public class AspectJExpressionPointcut implements Pointcut, MethodMatcher {

  private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();

  static {
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
    SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
  }

  private String expression;
  private ClassLoader pointcutClassLoader;
  private PointcutExpression pointcutExpression;

  @Override
  public MethodMatcher getMethodMatcher() {
    return this;
  }

  @Override
  public String getExpression() {
    return expression;
  }

  @Override
  public boolean matches(Method method) {
    checkReadyToMatch();

    ShadowMatch shadowMatch = getShadowMatch(method);
    return shadowMatch.alwaysMatches();
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  public void setPointcutClassLoader(ClassLoader pointcutClassLoader) {
    this.pointcutClassLoader = pointcutClassLoader;
  }

  public ClassLoader getPointcutClassLoader() {
    return pointcutClassLoader;
  }

  private void checkReadyToMatch() {
    if (Objects.isNull(getExpression())) {
      throw new IllegalArgumentException("Must set property 'expression' before attempting to match");
    }
    if (Objects.isNull(this.pointcutExpression)) {
      this.pointcutClassLoader = ClassUtils.getDefaultClassLoader();
      this.pointcutExpression = buildPointcutExpression(this.pointcutClassLoader);
    }
  }

  private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
    PointcutParser pointcutParser = PointcutParser
        .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, classLoader);
    return pointcutParser.parsePointcutExpression(replaceBooleanOperators(getExpression()));
  }

  private String replaceBooleanOperators(String pcExpr) {
    String result = StringUtils.replace(pcExpr, " and ", " && ");
    result = StringUtils.replace(result, " or ", " || ");
    result = StringUtils.replace(result, " not ", " ! ");
    return result;
  }

  private ShadowMatch getShadowMatch(Method method) {
    ShadowMatch shadowMatch;
    try {
      shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
    } catch (Exception e) {
      throw new RuntimeException("not implemented yet");
    }
    return shadowMatch;
  }

}
