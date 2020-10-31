package cn.mghio.beans.factory.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.factory.BeanFactory;
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
public class DefaultBeanFactory implements BeanFactory {

    private static final String BEAN_ID_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_ATTRIBUTE = "class";

    // <beanId, BeanDefinition>
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);

    public DefaultBeanFactory(String configFileLocation) {
        loadBeanDefinition(configFileLocation);
    }

    @Override
    public Object getBean(String beanId) {
        BeanDefinition bd = getBeanDefinition(beanId);
        if (null == bd) {
            throw new BeanCreationException("BeanDefinition does not exists, beanId:" + beanId);
        }
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
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

    @SuppressWarnings("unchecked")
    private void loadBeanDefinition(String configFileLocation) {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(configFileLocation)) {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element root = document.getRootElement();  // <beans>
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String beanId = element.attributeValue(BEAN_ID_ATTRIBUTE);
                String beanClassName = element.attributeValue(BEAN_CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(beanId, beanClassName);
                this.registerBeanDefinition(beanId, bd);
            }
        } catch (DocumentException | IOException e) {
            throw new BeanDefinitionException("IOException parsing XML document:" + configurationFile, e);
        }
    }
}
