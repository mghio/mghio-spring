package cn.mghio.aop.aspectj;

import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice {

  public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pc, Object adviceObject) {
    super(adviceMethod, pc, adviceObject);
  }

  @Override
  public Object invoke(MethodInvocation mi) throws Throwable {
    try {
      return mi.proceed();
    } catch (Throwable t) {
      this.invokeAdviceMethod();
      throw t;
    }
  }
}
