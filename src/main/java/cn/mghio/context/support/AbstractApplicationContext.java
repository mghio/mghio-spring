package cn.mghio.context.support;

import cn.mghio.beans.factory.anontation.AutowiredAnnotationProcessor;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.context.ApplicationContext;
import cn.mghio.core.io.Resource;

/**
 * @author mghio
 * @since 2020-11-01
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory beanFactory;

    public AbstractApplicationContext(String configFilePath) {
        beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = getResourceByPath(configFilePath);
        reader.loadBeanDefinition(resource);
        registerBeanPostProcessor(beanFactory);
    }

    @Override
    public Object getBean(String beanId) {
        return beanFactory.getBean(beanId);
    }

    protected void registerBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
        AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);
    }

    protected abstract Resource getResourceByPath(String configFilePath);
}
