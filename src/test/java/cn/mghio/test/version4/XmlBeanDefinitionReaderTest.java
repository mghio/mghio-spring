package cn.mghio.test.version4;

import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2020-11-28
 */
public class XmlBeanDefinitionReaderTest extends AbstractBeanDefinitionTest {

    @Test
    public void testReadBeanDefinitionFromXmlFile() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("orderservice-version4.xml");
        reader.loadBeanDefinition(resource);

        super.testComponentAnnotation(factory);
    }

}
