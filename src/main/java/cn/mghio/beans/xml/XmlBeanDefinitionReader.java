package cn.mghio.beans.xml;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.core.io.Resource;
import cn.mghio.utils.ClassUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static com.sun.org.apache.xml.internal.security.utils.Constants.configurationFile;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class XmlBeanDefinitionReader {

    private static final String BEAN_ID_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_ATTRIBUTE = "class";

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public void loadBeanDefinition(Resource resource) {
        try (InputStream is = resource.getInputStream()) {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);
            Element root = document.getRootElement();  // <beans>
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element element = iterator.next();
                String beanId = element.attributeValue(BEAN_ID_ATTRIBUTE);
                String beanClassName = element.attributeValue(BEAN_CLASS_ATTRIBUTE);
                BeanDefinition bd = new GenericBeanDefinition(beanId, beanClassName);
                this.registry.registerBeanDefinition(beanId, bd);
            }
        } catch (DocumentException | IOException e) {
            throw new BeanDefinitionException("IOException parsing XML document:" + configurationFile, e);
        }
    }
}
