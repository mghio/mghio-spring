package cn.mghio.test.version4;

import cn.mghio.context.ApplicationContext;
import cn.mghio.context.support.ClassPathApplicationContext;
import cn.mghio.service.version4.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mghio
 * @since 2020-11-25
 */
public class ApplicationContextTestV4 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathApplicationContext("orderservice-version4.xml");
        OrderService orderService = (OrderService) ctx.getBean("orderService");

        assertNotNull(orderService);
        assertNotNull(orderService.getStockDao());
        assertNotNull(orderService.getTradeDao());
    }

}
