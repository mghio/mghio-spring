package cn.mghio.core.io;

import cn.mghio.utils.Assert;
import cn.mghio.utils.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mghio
 * @since 2020-11-27
 */
public class PackageResourceLoader {

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        Assert.notNull(classLoader, "ClassLoader must be not null");
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Resource[] getResources(String basePackage) {
        Assert.notNull(basePackage, "basePackage must not be null");
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);
        ClassLoader classLoader = getClassLoader();
        URL url = classLoader.getResource(location);
        Assert.notNull(url, "URL must not be null");
        File rootDir = new File(url.getFile());

        Set<File> matchingFile = retrieveMatchingFiles(rootDir);
        Resource[] result = new Resource[matchingFile.size()];
        int i = 0;
        for (File file : matchingFile) {
            result[i++] = new FileSystemResource(file);
        }
        return result;
    }

    private Set<File> retrieveMatchingFiles(File rootDir) {
        if (!rootDir.exists() || !rootDir.isDirectory() || !rootDir.canRead()) {
            // Skipping, because it not exists or not directory or can't read
            return Collections.emptySet();
        }

        Set<File> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(rootDir, result);

        return result;
    }

    private void doRetrieveMatchingFiles(File dir, Set<File> result) {
        File[] dirContents = dir.listFiles();
        if (dirContents == null) {
            // couldn't retrieve contents of dir
            return;
        }

        for (File content : dirContents) {
            // not directory
            if (!content.isDirectory()) {
                result.add(content);
                continue;
            }

            if (content.canRead()) {
                // Recursion
                doRetrieveMatchingFiles(content, result);
            }
        }
    }

}
