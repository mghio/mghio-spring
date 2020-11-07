package cn.mghio.test.version2;

import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.support.BeanDefinitionResolver;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.dao.version2.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class BeanDefinitionResolverTest {

    private DefaultBeanFactory factory = null;
    private XmlBeanDefinitionReader reader = null;
    private ClassPathResource resource = null;
    private BeanDefinitionResolver resolver = null;

    @BeforeEach
    public void beforeEach() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
        resource = new ClassPathResource("orderservice-version2.xml");
        reader.loadBeanDefinition(resource);
        resolver = new BeanDefinitionResolver(factory);
    }

    @Test
    public void testResolveTypedStringReference() {
        TypedStringValue typedStringValue = new TypedStringValue("mghio");
        Object value = resolver.resolveValueIfNecessary(typedStringValue);

        assertNotNull(value);
        assertEquals("mghio", value);
    }

    @Test
    public void testResolveRuntimeReference() {
        RuntimeBeanReference reference = new RuntimeBeanReference("tradeService");
        Object value = resolver.resolveValueIfNecessary(reference);

        assertNotNull(value);
        assertTrue(value instanceof TradeService);
    }

}
