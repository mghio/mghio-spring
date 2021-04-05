package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.aop.aspectj.AspectJExpressionPointcut;
import cn.mghio.aop.framework.Advised;
import cn.mghio.aop.framework.AdvisedSupport;
import cn.mghio.aop.framework.AopProxyFactory;
import cn.mghio.aop.framework.CglibProxyFactory;
import cn.mghio.service.version5.OrderService;
import cn.mghio.tx.TransactionManager;
import cn.mghio.utils.MessageTracker;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class CglibAopProxyTest {

  private static AspectJBeforeAdvice beforeAdvice = null;
  private static AspectJAfterReturningAdvice afterAdvice = null;
  private static AspectJExpressionPointcut pc = null;
  private OrderService orderService = null;
  private TransactionManager tx = null;

  @BeforeEach
  public void setUp() throws Exception {
    MessageTracker.cleanMsg();
    orderService = new OrderService();
    tx = new TransactionManager();
    String expression = "execution(* cn.mghio.service.version5.*.placeOrder(..))";
    pc = new AspectJExpressionPointcut();
    pc.setExpression(expression);

    beforeAdvice = new AspectJBeforeAdvice(
        TransactionManager.class.getMethod("start"),
        pc,
        tx
    );

    afterAdvice = new AspectJAfterReturningAdvice(
      TransactionManager.class.getMethod("commit"),
      pc,
      tx
    );
  }

  @Test
  public void testGetProxy() {
    Advised advised = new AdvisedSupport();
    advised.addAdvice(beforeAdvice);
    advised.addAdvice(afterAdvice);
    advised.setTargetObject(orderService);

    AopProxyFactory factory = new CglibProxyFactory(advised);
    OrderService proxy = (OrderService)factory.getProxy();
    proxy.placeOrder();
    List<String> msgs = MessageTracker.getMsgs();
    assertEquals(3, msgs.size());
    assertEquals("start tx", msgs.get(0));
    assertEquals("place order", msgs.get(1));
    assertEquals("commit tx", msgs.get(2));
  }

}
