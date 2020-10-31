package cn.mghio.test.version1;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.service.version1.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test for {@link cn.mghio.beans.factory.BeanFactory}
 *
 * @author mghio
 * @since 2020-10-31
 */
public class BeanFactoryTest {

    private BeanFactory beanFactory;

    @BeforeEach
    public void beforeEach() {
        beanFactory = new DefaultBeanFactory("orderservice-version1.xml");
    }

    @Test
    public void testGetBeanFromXmlFile() {
        BeanDefinition bd = beanFactory.getBeanDefinition("orderService");

        assertEquals("cn.mghio.service.version1.OrderService", bd.getBeanClassNam());
        OrderService orderService = (OrderService) beanFactory.getBean("orderService");
        assertNotNull(orderService);
    }

    @Test
    public void testGetBeanFromXmlFileWithInvalidBeanId() {
        assertThrows(BeanCreationException.class, () -> beanFactory.getBean("notExistsBeanId"));
    }

    @Test
    public void testGetFromXmlFilWithFileNotExists() {
        assertThrows(BeanDefinitionException.class, () -> new DefaultBeanFactory("notExists.xml"));
    }

}
