package cn.mghio.test.version4;

import cn.mghio.core.io.PackageResourceLoader;
import cn.mghio.core.io.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author mghio
 * @since 2020-11-27
 */
public class PackageResourceLoaderTest {

    @Test
    public void testGetResource() {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("cn.mghio.dao.version4");

        assertEquals(2, resources.length);
    }

}
