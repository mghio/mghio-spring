package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.mghio.aop.config.MethodLocatingFactory;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.tx.TransactionManager;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class MethodLocatingFactoryTest {

  @Test
  public void testGetMethod() throws Exception {
    DefaultBeanFactory factory = new DefaultBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
    Resource resource = new ClassPathResource("orderservice-version5.xml");
    reader.loadBeanDefinition(resource);

    MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
    // 有顺序
    methodLocatingFactory.setTargetBeanName("tx");
    methodLocatingFactory.setMethodName("start");
    methodLocatingFactory.setBeanFactory(factory);

    Method m = methodLocatingFactory.getObject();
    assertEquals(m.getDeclaringClass(), TransactionManager.class);
    assertEquals(TransactionManager.class.getMethod("start"), m);
  }

}
