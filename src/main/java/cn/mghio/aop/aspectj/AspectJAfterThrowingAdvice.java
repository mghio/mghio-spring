package cn.mghio.aop.aspectj;

import cn.mghio.aop.config.AopInstanceFactory;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AspectJAfterThrowingAdvice extends AbstractAspectJAdvice {

  public AspectJAfterThrowingAdvice(Method adviceMethod, AspectJExpressionPointcut pc,
      AopInstanceFactory aopInstanceFactory) {
    super(adviceMethod, pc, aopInstanceFactory);
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
