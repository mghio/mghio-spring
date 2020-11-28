package cn.mghio.test.version4;

import cn.mghio.core.annotation.AnnotationAttributes;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.type.classreading.AnnotationMetadataReadingVisitor;
import cn.mghio.core.type.classreading.ClassMetadataReadingVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-25
 */
public class ClassReaderTest {

    private ClassReader classReader = null;

    @BeforeEach
    public void beforeEach() throws IOException {
        ClassPathResource resource = new ClassPathResource("cn/mghio/service/version4/OrderService.class");
        classReader = new ClassReader(resource.getInputStream());
    }

    @Test
    public void testGetClassMetadata() {
        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        assertFalse(visitor.isAbstract());
        assertFalse(visitor.isInterface());
        assertEquals("cn.mghio.service.version4.OrderService", visitor.getClassName());
        assertEquals("java.lang.Object", visitor.getSuperClassName());
        assertEquals(0, visitor.getInterfaceNames().length);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        String annotationType = "cn.mghio.stereotype.Component";
        assertTrue(visitor.hasAnnotation(annotationType));

        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotationType);
        assertEquals("orderService", attributes.get("value"));
    }

}
