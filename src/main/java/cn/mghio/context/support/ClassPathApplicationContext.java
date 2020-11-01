package cn.mghio.context.support;

import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.beans.xml.XmlBeanDefinitionReader;
import cn.mghio.context.ApplicationContext;
import cn.mghio.core.io.ClassPathResource;
import cn.mghio.core.io.Resource;
import cn.mghio.utils.ClassUtils;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class ClassPathApplicationContext extends AbstractApplicationContext {

    private ClassLoader classLoader;

    private DefaultBeanFactory beanFactory;

    public ClassPathApplicationContext(String configFilePath) {
        super(configFilePath);
    }

    @Override
    protected Resource getResourceByPath(String configFilePath) {
        return new ClassPathResource(configFilePath);
    }

}
