package cn.mghio.core.type;

import cn.mghio.core.annotation.AnnotationAttributes;

import java.util.Set;

/**
 * @author mghio
 * @since 2020-11-26
 */
public interface AnnotationMetadata extends ClassMetadata {

    Set<String> getAnnotationTypes();

    boolean hasAnnotation(String annotationType);

    AnnotationAttributes getAnnotationAttributes(String annotationType);

}
