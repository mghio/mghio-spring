package cn.mghio.test.version1;

import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.FileSystemResource;
import cn.mghio.core.io.Resource;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class ResourceTest {

    private Resource resource;

    @Test
    public void testClassPathResource() throws IOException {
        resource = new ClassPathResource("orderservice-version1.xml");

        assertNotNull(resource);
        assertNotNull(resource.getInputStream());
    }

    @Test
    public void testFileSystemResource() throws IOException {
        resource = new FileSystemResource("src/test/resources/orderservice-version1.xml");

        assertNotNull(resource);
        assertNotNull(resource.getInputStream());
    }

}
