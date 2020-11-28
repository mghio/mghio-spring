package cn.mghio.context.annotation;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.factory.anontation.AnnotatedBeanDefinition;
import cn.mghio.beans.factory.support.BeanNameGenerator;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.core.annotation.AnnotationAttributes;
import cn.mghio.core.type.AnnotationMetadata;
import cn.mghio.utils.ClassUtils;
import cn.mghio.utils.StringUtils;

import java.beans.Introspector;
import java.util.Set;

/**
 * @author mghio
 * @since 2020-11-28
 */
public class AnnotationBeanNameGenerator implements BeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        if (definition instanceof AnnotatedBeanDefinition) {
            String beanName = determineBeanNameFromAnnotation((AnnotatedBeanDefinition) definition);
            if (StringUtils.hasText(beanName)) {
                return beanName;
            }
        }

        return buildDefaultBeanName(definition);
    }

    private String buildDefaultBeanName(BeanDefinition definition) {
        String shortClassName = ClassUtils.getShortName(definition.getBeanClassName());
        return Introspector.decapitalize(shortClassName);
    }

    private String determineBeanNameFromAnnotation(AnnotatedBeanDefinition definition) {
        AnnotationMetadata metadata = definition.getMetadata();
        Set<String> types = metadata.getAnnotationTypes();
        String beanName = null;
        for (String type : types) {
            AnnotationAttributes attributes = metadata.getAnnotationAttributes(type);
            if (attributes.get("value") != null) {
                Object value = attributes.get("value");
                if (value instanceof String) {
                    String stringVal = (String) value;
                    if (StringUtils.hasLength(stringVal)) {
                        beanName = stringVal;
                    }
                }
            }
        }
        return beanName;
    }

}
