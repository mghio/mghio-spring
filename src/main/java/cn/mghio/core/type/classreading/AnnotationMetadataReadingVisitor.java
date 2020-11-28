package cn.mghio.core.type.classreading;

import cn.mghio.core.annotation.AnnotationAttributes;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.utils.StringUtils;
import lombok.NoArgsConstructor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mghio
 * @since 2020-11-25
 */
@NoArgsConstructor
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {

    private final Set<String> annotationSet = new LinkedHashSet<>(8);

    private final Map<String, AnnotationAttributes> attributesMap = new LinkedHashMap<>(8);

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String className = Type.getType(descriptor).getClassName();
        this.annotationSet.add(className);
        return new AnnotationAttributesReadingVisitor(className, this.attributesMap);
    }

    @Override
    public boolean hasSuperClass() {
        return StringUtils.hasText(getSuperClassName());
    }

    @Override
    public Set<String> getAnnotationTypes() {
        return this.annotationSet;
    }

    @Override
    public boolean hasAnnotation(String annotationType) {
        return this.annotationSet.contains(annotationType);
    }

    @Override
    public AnnotationAttributes getAnnotationAttributes(String annotationType) {
        return this.attributesMap.get(annotationType);
    }
}
