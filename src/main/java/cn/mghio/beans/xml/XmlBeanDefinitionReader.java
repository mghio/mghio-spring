package cn.mghio.beans.xml;

import cn.mghio.beans.*;
import cn.mghio.beans.exception.BeanDefinitionException;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.context.annotation.ClassPathBeanDefinitionScanner;
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
    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    public static final String BEAN_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

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
                String namespaceUri = element.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)) {
                    parseDefaultElement(element);
                } else if (this.isContextNamespace(namespaceUri)) {
                    parseComponentElement(element);
                }
                parseDefaultElement(element);
            }
        } catch (DocumentException | IOException e) {
            throw new BeanDefinitionException("IOException parsing XML document:" + resource, e);
        }
    }

    private void parseComponentElement(Element element) {
        String basePackages = element.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScanAndRegistry(basePackages);
    }

    private boolean isContextNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    private boolean isDefaultNamespace(String namespaceUri) {
        return (StringUtils.hasLength(namespaceUri) && BEAN_NAMESPACE_URI.equals(namespaceUri));
    }

    private void parseDefaultElement(Element element) {
        String beanId = element.attributeValue(BEAN_ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(BEAN_CLASS_ATTRIBUTE);
        BeanDefinition bd = new GenericBeanDefinition(beanId, beanClassName);
        if (null != element.attributeValue(BEAN_SCOPE_ATTRIBUTE)) {
            bd.setScope(element.attributeValue(BEAN_SCOPE_ATTRIBUTE));
        }
        parseConstructorArgElements(element, bd);
        parsePropertyElementValues(element, bd);
        this.registry.registerBeanDefinition(beanId, bd);
    }

    private void parseConstructorArgElements(Element rootEle, BeanDefinition bd) {
        Iterator<Element> iterator = rootEle.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iterator.hasNext()) {
            Element element = iterator.next();
            parseConstructorArgElement(element, bd);
        }
    }

    private void parseConstructorArgElement(Element element, BeanDefinition bd) {
        String typeAttr = element.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = element.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyElementValue(element, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }
        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }

    private void parsePropertyElementValues(Element element, BeanDefinition bd) {
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
