package cn.mghio.core.type.classreading;

import cn.mghio.core.io.Resource;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.core.type.ClassMetadata;

/**
 * @author mghio
 * @since 2020-11-26
 */
public interface MetadataReader {

    Resource getResource();

    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();

}
