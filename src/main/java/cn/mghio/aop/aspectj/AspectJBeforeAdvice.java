package cn.mghio.aop.aspectj;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

  public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pc, Object adviceObject) {
    super(adviceMethod, pc, adviceObject);
  }

  @Override
  public Object invoke(MethodInvocation mi) throws Throwable {
    this.invokeAdviceMethod();
    return mi.proceed();
  }

}
