package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.utils.ClassUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.sun.org.apache.xml.internal.security.utils.Constants.configurationFile;

/**
 * @author mghio
 * @since 2020-10-31
 */
public class DefaultBeanFactory implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    private ClassLoader classLoader = null;

    // <beanId, BeanDefinition>
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);

    public DefaultBeanFactory() {

    }

    @Override
    public Object getBean(String beanId) {
        BeanDefinition bd = getBeanDefinition(beanId);
        if (null == bd) {
            throw new BeanCreationException("BeanDefinition does not exists, beanId:" + beanId);
        }
        ClassLoader classLoader = this.getClassLoader();
        String beanClassName = bd.getBeanClassNam();
        try {
            Class<?> clazz = classLoader.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new BeanCreationException("Created bean for " + beanClassName + " fail.", e);
        }
    }

    @Override
    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanId, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return beanDefinitionMap.get(beanId);
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return (null != classLoader) ? classLoader : ClassUtils.getDefaultClassLoader();
    }
}
