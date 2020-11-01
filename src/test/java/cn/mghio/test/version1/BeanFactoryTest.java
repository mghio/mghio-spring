package cn.mghio.test.version1;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.BeanScope;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.service.version1.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class BeanFactoryTest {

    private Resource resource;
    private DefaultBeanFactory beanFactory;
    private XmlBeanDefinitionReader reader;

    @BeforeEach
    public void beforeEach() {
        resource = new ClassPathResource("orderservice-version1.xml");
        beanFactory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(beanFactory);
    }

    @Test
    public void testGetBeanFromXmlFile() {
        reader.loadBeanDefinition(resource);
        BeanDefinition bd = beanFactory.getBeanDefinition("orderService");

        assertTrue(bd.isSingleton());
        assertFalse(bd.isPrototype());
        assertEquals(BeanScope.DEFAULT, bd.getScope());
        assertEquals("cn.mghio.service.version1.OrderService", bd.getBeanClassNam());
        OrderService orderService = (OrderService) beanFactory.getBean("orderService");
        assertNotNull(orderService);

        OrderService orderServiceSecond = (OrderService) beanFactory.getBean("orderService");
        assertEquals(orderService, orderServiceSecond);
    }

    @Test
    public void testGetPrototypeBeanFromXmlFile() {
        reader.loadBeanDefinition(resource);
        BeanDefinition bd = beanFactory.getBeanDefinition("orderServicePrototype");

        assertFalse(bd.isSingleton());
        assertTrue(bd.isPrototype());
        OrderService orderService = (OrderService) beanFactory.getBean("orderServicePrototype");
        assertNotNull(orderService);
        OrderService orderServiceSecond = (OrderService) beanFactory.getBean("orderServicePrototype");
        assertNotEquals(orderService, orderServiceSecond);
    }

    @Test
    public void testGetBeanFromXmlFileWithInvalidBeanId() {
        assertThrows(BeanCreationException.class, () -> beanFactory.getBean("notExistsBeanId"));
    }

    @Test
    public void testGetFromXmlFilWithFileNotExists() {
        resource = new ClassPathResource("notExists.xml");
        assertThrows(BeanDefinitionException.class, () -> reader.loadBeanDefinition(resource));
    }

}
