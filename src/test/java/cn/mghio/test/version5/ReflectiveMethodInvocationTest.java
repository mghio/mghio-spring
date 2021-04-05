package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJAfterThrowingAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.aop.framework.ReflectiveMethodInvocation;
import cn.mghio.service.version5.OrderService;
import cn.mghio.tx.TransactionManager;
import cn.mghio.utils.MessageTracker;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class ReflectiveMethodInvocationTest {

  private AspectJBeforeAdvice beforeAdvice = null;
  private AspectJAfterReturningAdvice afterAdvice = null;
  private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
  private OrderService orderService = null;
  private TransactionManager tx;

  @BeforeEach
  public void setUp() throws Exception {
    orderService = new OrderService();
    tx = new TransactionManager();

    MessageTracker.cleanMsg();

    beforeAdvice = new AspectJBeforeAdvice(
        TransactionManager.class.getMethod("start"),
        null,
        tx
    );

    afterAdvice = new AspectJAfterReturningAdvice(
        TransactionManager.class.getMethod("commit"),
        null,
        tx
    );

    afterThrowingAdvice = new AspectJAfterThrowingAdvice(
        TransactionManager.class.getMethod("rollback"),
        null,
        tx
    );
  }

  @Test
  public void testMethodInvocation() throws Throwable {
    Method targetMethod = OrderService.class.getMethod("placeOrder");
    List<MethodInterceptor> interceptors = new ArrayList<>();
    interceptors.add(beforeAdvice);
    interceptors.add(afterAdvice);
    ReflectiveMethodInvocation rmi = new ReflectiveMethodInvocation(orderService, targetMethod,
        new Object[0], interceptors);
    rmi.proceed();

    List<String> msgs = MessageTracker.getMsgs();
    assertEquals(3, msgs.size());
    assertEquals("start tx", msgs.get(0));
    assertEquals("place order", msgs.get(1));
    assertEquals("commit tx", msgs.get(2));
  }

  @Test
  public void testMethodInvocationWithReverseOrder() throws Throwable {
    Method targetMethod = OrderService.class.getMethod("placeOrder");
    List<MethodInterceptor> interceptors = new ArrayList<>();
    interceptors.add(afterAdvice);
    interceptors.add(beforeAdvice);
    ReflectiveMethodInvocation rmi = new ReflectiveMethodInvocation(orderService, targetMethod,
        new Object[0], interceptors);
    rmi.proceed();

    List<String> msgs = MessageTracker.getMsgs();
    assertEquals(3, msgs.size());
    assertEquals("start tx", msgs.get(0));
    assertEquals("place order", msgs.get(1));
    assertEquals("commit tx", msgs.get(2));
  }

  @Test
  public void testMethodInvocationWithAfterThrowing() throws Throwable {
    Method targetMethod = OrderService.class.getMethod("placeOrderWithArithmeticException");
    List<MethodInterceptor> interceptors = new ArrayList<>();
    interceptors.add(afterThrowingAdvice);
    interceptors.add(beforeAdvice);

    ReflectiveMethodInvocation rmi = new ReflectiveMethodInvocation(orderService, targetMethod,
        new Object[0], interceptors);
    try {
      rmi.proceed();
    } catch (Exception e) {
      List<String> msgs = MessageTracker.getMsgs();
      assertEquals(2, msgs.size());
      assertEquals("start tx", msgs.get(0));
      assertEquals("rollback tx", msgs.get(1));
      return;
    }

    fail("no ArithmeticException throw");
  }

}
