package cn.mghio.test.version3;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.ConstructorArgument;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class BeanDefinitionTestV3 {

    @Test
    public void testConstructorArgument() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("orderservice-version3.xml");
        reader.loadBeanDefinition(resource);

        BeanDefinition bd = factory.getBeanDefinition("orderService");
        assertEquals("cn.mghio.service.version3.OrderService", bd.getBeanClassName());

        ConstructorArgument constructorArgument = bd.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> argumentsValues = constructorArgument.getArgumentsValues();

        assertEquals(3, argumentsValues.size());

        {
            RuntimeBeanReference ref = (RuntimeBeanReference) argumentsValues.get(0).getValue();
            assertEquals("stockService", ref.getBeanName());
        }
        {
            RuntimeBeanReference ref = (RuntimeBeanReference) argumentsValues.get(1).getValue();
            assertEquals("tradeService", ref.getBeanName());
        }
        {
            TypedStringValue typedStringValue = (TypedStringValue) argumentsValues.get(2).getValue();
            assertEquals("mghio", typedStringValue.getValue());
            assertEquals("java.lang.String", argumentsValues.get(2).getType());
        }
    }

}
