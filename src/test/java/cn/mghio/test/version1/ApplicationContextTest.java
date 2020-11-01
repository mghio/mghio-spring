package cn.mghio.test.version1;

import cn.mghio.context.ApplicationContext;
import cn.mghio.context.support.ClassPathApplicationContext;
import cn.mghio.context.support.FileSystemApplicationContext;
import cn.mghio.service.version1.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class ApplicationContextTest {

    @Test
    public void testGetBeanFromClassPathApplicationContext() {
        ApplicationContext ctx = new ClassPathApplicationContext("orderservice-version1.xml");
        OrderService orderService = (OrderService) ctx.getBean("orderService");

        assertNotNull(orderService);
    }

    @Test
    public void testGetBeanFromFileSystemApplicationContext() {
        ApplicationContext ctx = new FileSystemApplicationContext("src/test/resources/orderservice-version1.xml");
        OrderService orderService = (OrderService) ctx.getBean("orderService");

        assertNotNull(orderService);
    }

}
