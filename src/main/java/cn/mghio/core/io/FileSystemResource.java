package cn.mghio.core.io;

import cn.mghio.utils.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class FileSystemResource implements Resource {

    private final String path;

    private final File file;

    public FileSystemResource(String path) {
        Assert.notNull(path, "file path must not be null");
        this.path = path;
        this.file = new File(path);
    }

    public FileSystemResource(File file) {
        Assert.notNull(file, "file must not be null");
        this.path = file.getPath();
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }
}
