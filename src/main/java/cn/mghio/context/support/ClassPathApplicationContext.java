package cn.mghio.context.support;

import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class ClassPathApplicationContext extends AbstractApplicationContext {

    public ClassPathApplicationContext(String configFilePath) {
        super(configFilePath);
    }

    @Override
    protected Resource getResourceByPath(String configFilePath) {
        return new ClassPathResource(configFilePath);
    }

}
