package cn.mghio.context.annotation;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanDefinitionStoreException;
import cn.mghio.beans.factory.support.BeanNameGenerator;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.core.io.PackageResourceLoader;
import cn.mghio.core.io.Resource;
import cn.mghio.core.type.classreading.MetadataReader;
import cn.mghio.core.type.classreading.SimpleMetadataReader;
import cn.mghio.stereotype.Component;
import cn.mghio.utils.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mghio
 * @since 2020-11-27
 */
public class ClassPathBeanDefinitionScanner {

    public static final String SEMICOLON_SEPARATOR = ",";

    private final BeanDefinitionRegistry registry;

    private final PackageResourceLoader resourceLoader = new PackageResourceLoader();

    private final BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScanAndRegistry(String packageToScan) {
        String[] basePackages = StringUtils.tokenizeToStringArray(packageToScan, SEMICOLON_SEPARATOR);

        Set<BeanDefinition> beanDefinitions = new HashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                registry.registerBeanDefinition(candidate.getId(), candidate);
            }
        }
        return beanDefinitions;
    }

    private Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new HashSet<>();
        try {
            Resource[] resources = this.resourceLoader.getResources(basePackage);
            for (Resource resource : resources) {
                MetadataReader metadataReader = new SimpleMetadataReader(resource);
                if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                    ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                    String beanName = this.beanNameGenerator.generateBeanName(sbd, registry);
                    sbd.setId(beanName);
                    candidates.add(sbd);
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }
}
