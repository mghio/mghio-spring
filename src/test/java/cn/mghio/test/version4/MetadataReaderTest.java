package cn.mghio.test.version4;

import cn.mghio.core.annotation.AnnotationAttributes;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.core.type.classreading.MetadataReader;
import cn.mghio.core.type.classreading.SimpleMetadataReader;
import cn.mghio.stereotype.Component;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-26
 */
public class MetadataReaderTest {

    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource resource = new ClassPathResource("cn/mghio/service/version4/OrderService.class");

        MetadataReader reader = new SimpleMetadataReader(resource);
        AnnotationMetadata metadata = reader.getAnnotationMetadata();

        String annotationType = Component.class.getName();
        assertTrue(metadata.hasAnnotation(annotationType));
        AnnotationAttributes attributes = metadata.getAnnotationAttributes(annotationType);
        assertEquals("orderService", attributes.get("value"));

        assertFalse(metadata.isAbstract());
        assertFalse(metadata.isFinal());
        assertFalse(metadata.isInterface());
        assertEquals("cn.mghio.service.version4.OrderService", metadata.getClassName());
    }

}
