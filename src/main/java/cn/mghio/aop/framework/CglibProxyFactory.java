package cn.mghio.aop.framework;

import cn.mghio.aop.Advice;
import cn.mghio.aop.cglib.core.SpringNamingPolicy;
import cn.mghio.utils.Assert;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import net.sf.cglib.core.CodeGenerationException;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class CglibProxyFactory implements AopProxyFactory {

  /*
   * Constants for CGLIB callback array indices
   */
  private static final int AOP_PROXY = 0;

  /** Logger available to subclasses; static to optimize serialization */
  protected static final Log logger = LogFactory.getLog(CglibProxyFactory.class);

  protected final Advised advised;

  public CglibProxyFactory(Advised config) {
    Assert.notNull(config, "AdvisedSupport must not be null");
    if (config.getAdvices().size() == 0) {
      throw new AopConfigException("No advisors and no TargetSource specified");
    }

    this.advised = config;
  }

  @Override
  public Object getProxy() {
    return getProxy(null);
  }

  @Override
  public Object getProxy(ClassLoader classLoader) {
    if (logger.isDebugEnabled()) {
      logger.debug("Creating CGLIB proxy: target class is " + this.advised.getTargetClass());
    }

    try {
      Class<?> rootClass = this.advised.getTargetClass();

      // Configure CGLIB Enhancer...
      Enhancer enhancer = new Enhancer();
      if (classLoader != null) {
        enhancer.setClassLoader(classLoader);
      }
      enhancer.setSuperclass(rootClass);
      enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);  // BySpringCGLIB
      enhancer.setInterceptDuringConstruction(false);

      Callback[] callbacks = getCallbacks(rootClass);
      Class<?>[] types = new Class<?>[callbacks.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = callbacks[i].getClass();
      }
      enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised));
      enhancer.setCallbackTypes(types);
      enhancer.setCallbacks(callbacks);

      // Generate the proxy class and create a proxy instance.
      return enhancer.create();
    }
    catch (CodeGenerationException | IllegalArgumentException ex) {
      throw new AopConfigException("Could not generate CGLIB subclass of class [" +
          this.advised.getTargetClass() + "]: " +
          "Common causes of this problem include using a final class or a non-visible class",
          ex);
    } catch (Exception ex) {
      // TargetSource.getTarget() failed
      throw new AopConfigException("Unexpected AOP exception", ex);
    }
  }

  private Callback[] getCallbacks(Class<?> rootClass) {
    // Choose an "aop" interceptor (used for AOP calls).
    Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);

    return new Callback[] {
        aopInterceptor,  // AOP_PROXY for normal advice
    };
  }

  /**
   * General purpose AOP callback. Used when the target is dynamic or when the
   * proxy is not frozen.
   */
  private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

    private final Advised advised;

    public DynamicAdvisedInterceptor(Advised advised) {
      this.advised = advised;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable {
      Object target = this.advised.getTargetObject();
      List<Advice> chain = this.advised.getAdvices(method);

      Object retVal;
      // Check whether we only have one InvokerInterceptor: that is,
      // no real advice, but just reflective invocation of the target.
      if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
        // We can skip creating a MethodInvocation: just invoke the target directly.
        // Note that the final invoker must be an InvokerInterceptor, so we know
        // it does nothing but a reflective operation on the target, and no hot
        // swapping or fancy proxying.
        retVal = method.invoke(target, args);
      } else {
        List<org.aopalliance.intercept.MethodInterceptor> interceptors = new ArrayList<>(chain);
        // We need to create a method invocation...
        retVal = new ReflectiveMethodInvocation(target, method, args, interceptors).proceed();
      }
      return retVal;
    }
  }

  /**
   * CallbackFilter to assign Callbacks to methods.
   */
  private static class ProxyCallbackFilter implements CallbackFilter {

    private final Advised advised;

    public ProxyCallbackFilter(Advised advised) {
      this.advised = advised;
    }

    @Override
    public int accept(Method method) {
      return AOP_PROXY;
    }
  }

}
