package cn.mghio.aop.aspectj;

import cn.mghio.aop.Advice;
import cn.mghio.aop.Pointcut;
import java.lang.reflect.Method;

/**
 * @author mghio
 * @since 2021-04-05
 */
public abstract class AbstractAspectJAdvice implements Advice {

  protected Method adviceMethod;
  protected AspectJExpressionPointcut pc;
  protected Object adviceObject;

  public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPointcut pc, Object adviceObject) {
    this.adviceMethod = adviceMethod;
    this.pc = pc;
    this.adviceObject = adviceObject;
  }

  @Override
  public Pointcut getPointcut() {
    return pc;
  }

  protected void invokeAdviceMethod() throws Throwable {
    adviceMethod.invoke(adviceObject);
  }

}
