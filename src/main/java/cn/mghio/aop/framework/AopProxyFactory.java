package cn.mghio.aop.framework;

/**
 * @author mghio
 * @since 2021-04-05
 */
public interface AopProxyFactory {

  Object getProxy();

  Object getProxy(ClassLoader classLoader);

}
