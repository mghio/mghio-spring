package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.mghio.context.ApplicationContext;
import cn.mghio.context.support.ClassPathApplicationContext;
import cn.mghio.service.version5.OrderService;
import cn.mghio.utils.MessageTracker;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-04
 */
public class ApplicationContextTest5 {

  @BeforeEach
  public void setUp() {
    MessageTracker.cleanMsg();
  }

  @Test
  public void placeOrder() {
    ApplicationContext ctx = new ClassPathApplicationContext("orderservice-version5.xml");
    OrderService orderService = (OrderService) ctx.getBean("orderService");

    assertNotNull(orderService.getStockDao());
    assertNotNull(orderService.getTradeDao());
    orderService.placeOrder();

    List<String> msgs = MessageTracker.getMsgs();
    assertEquals(3, msgs.size());
    assertEquals("start tx", msgs.get(0));
    assertEquals("place order", msgs.get(1));
    assertEquals("commit tx", msgs.get(2));
  }

}
