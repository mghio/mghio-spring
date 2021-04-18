package cn.mghio.test.version5;

import cn.mghio.aop.config.AopInstanceFactory;
import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.tx.TransactionManager;
import java.lang.reflect.Method;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class AbstractVersion5Test {

  protected BeanFactory getBeanFactory(String configFile) {
    DefaultBeanFactory beanFactory = new DefaultBeanFactory();
    XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
    Resource resource = new ClassPathResource(configFile);
    reader.loadBeanDefinition(resource);
    return beanFactory;
  }

  protected Method getAdviceMethod(String methodName) throws Exception {
    return TransactionManager.class.getMethod(methodName);
  }

  protected AopInstanceFactory getAopInstanceFactory(String targetBeanName) {
    AopInstanceFactory factory = new AopInstanceFactory();
    factory.setAspectBeanName(targetBeanName);
    return factory;
  }

}
