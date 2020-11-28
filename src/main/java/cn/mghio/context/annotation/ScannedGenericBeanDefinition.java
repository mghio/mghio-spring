package cn.mghio.context.annotation;

import cn.mghio.beans.factory.anontation.AnnotatedBeanDefinition;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.core.type.AnnotationMetadata;

/**
 * @author mghio
 * @since 2020-11-26
 */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

    private AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();
        this.metadata = metadata;
        setBeanClassName(this.metadata.getClassName());
    }

    public ScannedGenericBeanDefinition(String beanId, String beanClassName, AnnotationMetadata metadata) {
        super(beanId, beanClassName);
        this.metadata = metadata;
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return this.metadata;
    }
}
