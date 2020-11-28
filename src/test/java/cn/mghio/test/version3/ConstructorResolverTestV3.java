package cn.mghio.test.version3;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.support.ConstructorResolver;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.service.version3.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class ConstructorResolverTestV3 {

    @Test
    public void testAutowireConstructor() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("orderservice-version3.xml");
        reader.loadBeanDefinition(resource);

        BeanDefinition bd = factory.getBeanDefinition("orderService");
        ConstructorResolver resolver = new ConstructorResolver(factory);
        OrderService orderService = (OrderService) resolver.autowireConstructor(bd);

        assertEquals("mghio", orderService.getOwner());
        assertNotNull(orderService.getStockDao());
        assertNotNull(orderService.getTradeDao());
    }

}
