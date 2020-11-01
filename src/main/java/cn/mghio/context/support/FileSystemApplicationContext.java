package cn.mghio.context.support;

import cn.mghio.core.io.FileSystemResource;
import cn.mghio.core.io.Resource;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class FileSystemApplicationContext extends AbstractApplicationContext {

    public FileSystemApplicationContext(String configFilePath) {
        super(configFilePath);
    }

    @Override
    protected Resource getResourceByPath(String configFilePath) {
        return new FileSystemResource(configFilePath);
    }

}
