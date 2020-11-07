package cn.mghio.test.version3;

import cn.mghio.context.ApplicationContext;
import cn.mghio.context.support.ClassPathApplicationContext;
import cn.mghio.service.version3.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class ApplicationContextTestV3 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathApplicationContext("orderservice-version3.xml");
        OrderService orderService = (OrderService) ctx.getBean("orderService");

        assertNotNull(orderService.getStockService());
        assertNotNull(orderService.getTradeService());
        assertNotNull(orderService.getOwner());
    }

}
