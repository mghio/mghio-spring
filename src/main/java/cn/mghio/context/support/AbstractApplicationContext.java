package cn.mghio.context.support;

import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.context.ApplicationContext;
import cn.mghio.core.io.Resource;
import cn.mghio.utils.ClassUtils;

/**
 * @author mghio
 * @since 2020-11-01
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private ClassLoader classLoader;

    private DefaultBeanFactory beanFactory;

    public AbstractApplicationContext(String configFilePath) {
        beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = getResourceByPath(configFilePath);
        reader.loadBeanDefinition(resource);
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = (null != classLoader) ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public ClassLoader getClassLoader() {
        return (null != classLoader) ? classLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override
    public Object getBean(String beanId) {
        return beanFactory.getBean(beanId);
    }

    protected abstract Resource getResourceByPath(String configFilePath);
}
