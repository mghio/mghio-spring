package cn.mghio.core.type.classreading;

import cn.mghio.core.io.Resource;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.core.type.ClassMetadata;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author mghio
 * @since 2020-11-26
 */
public class SimpleMetadataReader implements MetadataReader {

    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws IOException {
        ClassReader classReader;
        try (InputStream is = new BufferedInputStream(resource.getInputStream())) {
            classReader = new ClassReader(is);
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.resource = resource;
        this.classMetadata = visitor;
        this.annotationMetadata = visitor;
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }

}
