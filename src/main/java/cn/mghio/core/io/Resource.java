package cn.mghio.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mghio
 * @since 2020-11-01
 */
public interface Resource {

    InputStream getInputStream() throws IOException;

}
