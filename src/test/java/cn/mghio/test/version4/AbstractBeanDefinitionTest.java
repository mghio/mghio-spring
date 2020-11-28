package cn.mghio.test.version4;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.context.annotation.ScannedGenericBeanDefinition;
import cn.mghio.core.annotation.AnnotationAttributes;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mghio
 * @since 2020-11-28
 */
public abstract class AbstractBeanDefinitionTest {

    protected void testComponentAnnotation(DefaultBeanFactory beanFactory) {
        String annotationType = Component.class.getName();

        {
            BeanDefinition bd = beanFactory.getBeanDefinition("stockDao");
            assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata metadata = sbd.getMetadata();

            assertTrue(metadata.hasAnnotation(annotationType));
            AnnotationAttributes attributes = metadata.getAnnotationAttributes(annotationType);
            assertEquals("stockDao", attributes.get("value"));
        }

        {
            BeanDefinition bd = beanFactory.getBeanDefinition("tradeDao");
            assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata metadata = sbd.getMetadata();

            assertTrue(metadata.hasAnnotation(annotationType));
            AnnotationAttributes attributes = metadata.getAnnotationAttributes(annotationType);
            assertEquals("tradeDao", attributes.get("value"));
        }

        {
            BeanDefinition bd = beanFactory.getBeanDefinition("orderService");
            assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) bd;
            AnnotationMetadata metadata = sbd.getMetadata();

            assertTrue(metadata.hasAnnotation(annotationType));
            AnnotationAttributes attributes = metadata.getAnnotationAttributes(annotationType);
            assertEquals("orderService", attributes.get("value"));
        }
    }

}
