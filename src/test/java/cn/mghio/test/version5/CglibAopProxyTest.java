package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.aop.aspectj.AspectJExpressionPointcut;
import cn.mghio.aop.config.AopInstanceFactory;
import cn.mghio.aop.framework.Advised;
import cn.mghio.aop.framework.AdvisedSupport;
import cn.mghio.aop.framework.AopProxyFactory;
import cn.mghio.aop.framework.CglibProxyFactory;
import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.service.version5.OrderService;
import cn.mghio.utils.MessageTracker;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class CglibAopProxyTest extends AbstractVersion5Test {

  private static AspectJBeforeAdvice beforeAdvice = null;
  private static AspectJAfterReturningAdvice afterAdvice = null;
  private static AspectJExpressionPointcut pc = null;
  private OrderService orderService = null;
  private BeanFactory beanFactory = null;
  private AopInstanceFactory aopInstanceFactory = null;

  @BeforeEach
  public void setUp() throws Exception {
    MessageTracker.cleanMsg();
    orderService = new OrderService();
    beanFactory = getBeanFactory("orderservice-version5.xml");
    aopInstanceFactory = getAopInstanceFactory("tx");
    aopInstanceFactory.setBeanFactory(beanFactory);
    String expression = "execution(* cn.mghio.service.version5.*.placeOrder(..))";
    pc = new AspectJExpressionPointcut();
    pc.setExpression(expression);

    beforeAdvice = new AspectJBeforeAdvice(
        getAdviceMethod("start"),
        pc,
        aopInstanceFactory
    );

    afterAdvice = new AspectJAfterReturningAdvice(
        getAdviceMethod("commit"),
        pc,
        aopInstanceFactory
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
