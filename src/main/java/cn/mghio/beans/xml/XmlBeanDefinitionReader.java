package cn.mghio.beans.xml;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.TypedStringValue;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.core.io.Resource;
import cn.mghio.utils.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class XmlBeanDefinitionReader {

    private static final String BEAN_ID_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_ATTRIBUTE = "class";
    private static final String BEAN_SCOPE_ATTRIBUTE = "scope";
    private static final String PROPERTY_ATTRIBUTE = "property";
    public static final String REF_ATTRIBUTE = "ref";
    public static final String VALUE_ATTRIBUTE = "value";
    public static final String NAME_ATTRIBUTE = "name";

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
                if (null != element.attributeValue(BEAN_SCOPE_ATTRIBUTE)) {
                    bd.setScope(element.attributeValue(BEAN_SCOPE_ATTRIBUTE));
                }
                parsePropertyElementValue(element, bd);
                this.registry.registerBeanDefinition(beanId, bd);
            }
        } catch (DocumentException | IOException e) {
            throw new BeanDefinitionException("IOException parsing XML document:" + resource, e);
        }
    }

    private void parsePropertyElementValue(Element element, BeanDefinition bd) {
        Iterator<Element> iterator = element.elementIterator(PROPERTY_ATTRIBUTE);
        while (iterator.hasNext()) {
            Element propertyElement = iterator.next();
            String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasText(propertyName)) {
                return;
            }

            Object value = parsePropertyElementValue(propertyElement, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, value);
            bd.getPropertyValues().add(propertyValue);
        }

    }

    private Object parsePropertyElementValue(Element propertyElement, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "' " : "<constructor-arg> element";

        boolean hasRefAttribute = propertyElement.attribute(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = propertyElement.attribute(VALUE_ATTRIBUTE) != null;

        if (hasRefAttribute) {
            String refName = propertyElement.attributeValue(REF_ATTRIBUTE);
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        } else if (hasValueAttribute) {
            String value = propertyElement.attributeValue(VALUE_ATTRIBUTE);
            TypedStringValue valueHolder = new TypedStringValue(value);
            return valueHolder;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }
}
