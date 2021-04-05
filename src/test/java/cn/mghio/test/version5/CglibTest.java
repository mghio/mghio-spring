package cn.mghio.test.version5;

import cn.mghio.service.version5.OrderService;
import cn.mghio.tx.TransactionManager;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class CglibTest {

  @Test
  public void testCallback() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(OrderService.class);
    enhancer.setCallback(new TransactionInterceptor());
    OrderService orderService = (OrderService) enhancer.create();
    orderService.placeOrder();
    System.out.println(orderService.toString());
  }

  @Test
  public void testFilter() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(OrderService.class);
    enhancer.setInterceptDuringConstruction(false);

    Callback[] callbacks = new Callback[]{new TransactionInterceptor(), NoOp.INSTANCE};
    Class<?>[] types = new Class[callbacks.length];
    for (int i = 0; i < types.length; i++) {
      types[i] = callbacks[i].getClass();
    }
    enhancer.setCallbackFilter(new ProxyCallbackFilter());
    enhancer.setCallbacks(callbacks);
    enhancer.setCallbackTypes(types);

    OrderService orderService = (OrderService) enhancer.create();
    orderService.placeOrder();
    System.out.println(orderService.toString());
  }

  private static class TransactionInterceptor implements MethodInterceptor {
    TransactionManager txManager = new TransactionManager();

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable {
      txManager.start();
      Object result = proxy.invokeSuper(obj, args);
      txManager.commit();
      return result;
    }
  }

  private static class ProxyCallbackFilter implements CallbackFilter {

    public ProxyCallbackFilter() {
    }

    @Override
    public int accept(Method method) {
      if (method.getName().startsWith("place")) {
        return 0;
      } else {
        return 1;
      }
    }
  }

}
