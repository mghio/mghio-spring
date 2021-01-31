package cn.mghio.test.version4;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.mghio.beans.factory.anontation.AutowiredFieldElement;
import cn.mghio.beans.factory.anontation.InjectionElement;
import cn.mghio.beans.factory.anontation.InjectionMetadata;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.service.version4.OrderService;
import java.lang.reflect.Field;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class InjectionMetadataTest {

  @Test
  public void testInjection() throws Exception {
    DefaultBeanFactory factory = new DefaultBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
    Resource resource = new ClassPathResource("orderservice-version4.xml");
    reader.loadBeanDefinition(resource);

    Class<?> clz = OrderService.class;
    LinkedList<InjectionElement> elements = new LinkedList<>();

    {
      Field field = clz.getDeclaredField("stockDao");
      InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
      elements.add(injectionElement);
    }
    {
      Field field = clz.getDeclaredField("tradeDao");
      InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
      elements.add(injectionElement);
    }

    InjectionMetadata metadata = new InjectionMetadata(clz, elements);
    OrderService orderService = new OrderService();
    metadata.inject(orderService);

    assertNotNull(orderService.getStockDao());
    assertNotNull(orderService.getTradeDao());
  }

}
