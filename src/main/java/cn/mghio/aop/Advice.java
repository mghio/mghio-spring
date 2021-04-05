package cn.mghio.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author mghio
 * @since 2021-04-05
 */
public interface Advice extends MethodInterceptor {

  Pointcut getPointcut();

}
