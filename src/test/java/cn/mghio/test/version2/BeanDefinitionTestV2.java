package cn.mghio.test.version2;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class BeanDefinitionTestV2 {

    @Test
    public void testGetBeanDefinition() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);

        ClassPathResource resource = new ClassPathResource("orderservice-version2.xml");
        reader.loadBeanDefinition(resource);

        BeanDefinition bd = factory.getBeanDefinition("orderService");
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        assertEquals(4, propertyValues.size());

        {
            PropertyValue propertyValue = this.getPropertyValueByName(propertyValues, "stockService");
            assertNotNull(propertyValue);
            assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
        {
            PropertyValue propertyValue = this.getPropertyValueByName(propertyValues, "tradeService");
            assertNotNull(propertyValue);
            assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
        {
            PropertyValue propertyValue = this.getPropertyValueByName(propertyValues, "num");
            assertNotNull(propertyValue);
            assertTrue(propertyValue.getValue() instanceof TypedStringValue);
        }
        {
            PropertyValue propertyValue = this.getPropertyValueByName(propertyValues, "owner");
            assertNotNull(propertyValue);
            assertTrue(propertyValue.getValue() instanceof TypedStringValue);
        }
    }

    private PropertyValue getPropertyValueByName(List<PropertyValue> propertyValues, String name) {
        for (PropertyValue propertyValue : propertyValues) {
            if (name != null && name.equals(propertyValue.getName())) {
                return propertyValue;
            }
        }
        return null;
    }

}
