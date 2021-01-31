package cn.mghio.test.version4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import cn.mghio.beans.factory.anontation.AutowiredAnnotationProcessor;
import cn.mghio.beans.factory.anontation.AutowiredFieldElement;
import cn.mghio.beans.factory.anontation.InjectionElement;
import cn.mghio.beans.factory.anontation.InjectionMetadata;
import cn.mghio.beans.factory.config.DependencyDescriptor;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.dao.version4.StockDao;
import cn.mghio.dao.version4.TradeDao;
import cn.mghio.service.version4.OrderService;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class AutowiredAnnotationProcessorTest {

  private final TradeDao tradeDao = new TradeDao();
  private final StockDao stockDao = new StockDao();

  // mock resolveDependency() result
  private final DefaultBeanFactory factory = new DefaultBeanFactory() {
    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
      if (descriptor.getDependencyType().equals(TradeDao.class)) {
        return tradeDao;
      }
      if (descriptor.getDependencyType().equals(StockDao.class)) {
        return stockDao;
      }
      throw new RuntimeException("can't support types except TradeDao or StockDao");
    }
  };

  @Test
  public void testGetInjectionMetadata() {
    AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
    processor.setBeanFactory(factory);
    InjectionMetadata metadata = processor.buildAutowiringMetadata(OrderService.class);
    List<InjectionElement> elements = metadata.getInjectionElements();

    assertEquals(2, elements.size());
    assertFieldExist(elements, "tradeDao");
    assertFieldExist(elements, "stockDao");

    OrderService orderService = new OrderService();
    metadata.inject(orderService);

    assertNotNull(orderService.getStockDao());
    assertNotNull(orderService.getTradeDao());
  }

  private void assertFieldExist(List<InjectionElement> elements, String fieldName) {
    for (InjectionElement element : elements) {
      AutowiredFieldElement fieldElement = (AutowiredFieldElement) element;
      Field field = fieldElement.getField();
      if (field.getName().equals(fieldName)) {
        return;
      }
    }
    fail(fieldName + "does not exist!");
  }

}
