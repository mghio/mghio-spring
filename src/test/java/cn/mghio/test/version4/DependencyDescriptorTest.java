package cn.mghio.test.version4;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.mghio.beans.factory.config.DependencyDescriptor;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.dao.version4.TradeDao;
import cn.mghio.service.version4.OrderService;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class DependencyDescriptorTest {

  @Test
  public void testResolveDependency() throws Exception {
    DefaultBeanFactory factory = new DefaultBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
    Resource resource = new ClassPathResource("orderservice-version4.xml");
    reader.loadBeanDefinition(resource);

    Field field = OrderService.class.getDeclaredField("tradeDao");
    DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
    Object o = factory.resolveDependency(descriptor);
    assertTrue(o instanceof TradeDao);
  }

}
