package cn.mghio.aop.aspectj;

import cn.mghio.aop.Advice;
import cn.mghio.aop.Pointcut;
import cn.mghio.aop.config.AopInstanceFactory;
import java.lang.reflect.Method;

/**
 * @author mghio
 * @since 2021-04-05
 */
public abstract class AbstractAspectJAdvice implements Advice {

  protected Method adviceMethod;
  protected AspectJExpressionPointcut pc;
  protected AopInstanceFactory adviceObjectFactory;

  public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPointcut pc, AopInstanceFactory adviceObjectFactory) {
    this.adviceMethod = adviceMethod;
    this.pc = pc;
    this.adviceObjectFactory = adviceObjectFactory;
  }

  @Override
  public Pointcut getPointcut() {
    return pc;
  }

  protected void invokeAdviceMethod() throws Throwable {
    adviceMethod.invoke(adviceObjectFactory.getAspectInstance());
  }

  public Object getAdviceInstance() throws Exception {
    return adviceObjectFactory.getAspectInstance();
  }

  public Method getAdviceMethod() {
    return adviceMethod;
  }
}
