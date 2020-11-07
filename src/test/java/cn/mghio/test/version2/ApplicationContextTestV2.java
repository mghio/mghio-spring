package cn.mghio.test.version2;

import cn.mghio.context.ApplicationContext;
import cn.mghio.context.support.ClassPathApplicationContext;
import cn.mghio.service.version2.OrderService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-05
 */
public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathApplicationContext("orderservice-version2.xml");
        OrderService orderService = (OrderService) ctx.getBean("orderService");

        assertNotNull(orderService.getStockService());
        assertNotNull(orderService.getTradeService());
        assertNotNull(orderService.getOrderTime());

        assertEquals(2, orderService.getNum());
        assertEquals("mghio", orderService.getOwner());
    }

}
