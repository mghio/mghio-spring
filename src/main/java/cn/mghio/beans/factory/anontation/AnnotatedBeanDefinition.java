package cn.mghio.beans.factory.anontation;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.core.type.AnnotationMetadata;

/**
 * @author mghio
 * @since 2020-11-26
 */
public interface AnnotatedBeanDefinition extends BeanDefinition {

    AnnotationMetadata getMetadata();

}
