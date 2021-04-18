package cn.mghio.aop.config;

import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJAfterThrowingAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.aop.aspectj.AspectJExpressionPointcut;
import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.ConstructorArgument;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.support.BeanDefinitionReaderUtils;
import cn.mghio.beans.support.BeanDefinitionRegistry;
import cn.mghio.beans.support.GenericBeanDefinition;
import cn.mghio.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class ConfigBeanDefinitionParser {

  private static final String ASPECT = "aspect";
  private static final String EXPRESSION = "expression";
  private static final String ID = "id";
  private static final String REF = "ref";
  private static final String BEFORE = "before";
  private static final String AFTER = "after";
  private static final String AFTER_RETURNING_ELEMENT = "after-returning";
  private static final String AFTER_THROWING_ELEMENT = "after-throwing";
  private static final String AROUND = "around";
  private static final String POINTCUT = "pointcut";
  private static final String POINTCUT_REF = "pointcut-ref";
  private static final String ASPECT_NAME_PROPERTY = "aspectName";

  public void parse(Element element, BeanDefinitionRegistry registry) {
    List<Element> childElements = element.elements();
    for (Element el : childElements) {
      String localName = el.getName();
      if (ASPECT.equals(localName)) {
        parseAspect(el, registry);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void parseAspect(Element aspectElement, BeanDefinitionRegistry registry) {
    String aspectName = aspectElement.attributeValue(REF);

    List<BeanDefinition> beanDefinitions = new ArrayList<>();
    List<RuntimeBeanReference> beanReferences = new ArrayList<>();

    List<Element> elements = aspectElement.elements();
    boolean adviceFoundAlready = false;
    for (Element element : elements) {
      if (isAdviceNode(element)) {
        if (!adviceFoundAlready) {
          adviceFoundAlready = true;
          if (!StringUtils.hasText(aspectName)) {
            return;
          }
          beanReferences.add(new RuntimeBeanReference(aspectName));
        }
        GenericBeanDefinition advisorDefinition = parseAdvice(aspectName, element, registry,
            beanDefinitions, beanReferences);
        beanDefinitions.add(advisorDefinition);
      }
    }

    List<Element> pointcuts = aspectElement.elements(POINTCUT);
    for (Element pointcut : pointcuts) {
      parsePointcut(pointcut, registry);
    }
  }

  /**
   * Parses the supplied {@code <pointcut>} and registers the resulting
   * Pointcut with the BeanDefinitionRegistry.
   */
  private void parsePointcut(Element pointcutElement, BeanDefinitionRegistry registry) {
    String id = pointcutElement.attributeValue(ID);
    String expression = pointcutElement.attributeValue(EXPRESSION);

    GenericBeanDefinition pointcutDefinition = createPointcutDefinition(expression);
    if (StringUtils.hasText(id)) {
      registry.registerBeanDefinition(id, pointcutDefinition);
    } else {
      BeanDefinitionReaderUtils.registerWithGeneratedName(pointcutDefinition, registry);
    }
  }

  private GenericBeanDefinition parseAdvice(String aspectName, Element adviceElement,
      BeanDefinitionRegistry registry, List<BeanDefinition> beanDefinitions,
      List<RuntimeBeanReference> beanReferences) {

    GenericBeanDefinition methodDefinition = new GenericBeanDefinition(MethodLocatingFactory.class);
    methodDefinition.getPropertyValues().add(new PropertyValue("targetBeanName", aspectName));
    methodDefinition.getPropertyValues().add(new PropertyValue("methodName",
        adviceElement.attributeValue("method")));
    methodDefinition.setSynthetic(true);

    // create instance definition factory
    GenericBeanDefinition aspectFactoryDef = new GenericBeanDefinition(AopInstanceFactory.class);
    aspectFactoryDef.getPropertyValues().add(new PropertyValue("aspectBeanName", aspectName));
    aspectFactoryDef.setSynthetic(true);

    // register the pointcut
    GenericBeanDefinition adviceDef = createAdviceDefinition(adviceElement, aspectName,
        methodDefinition, aspectFactoryDef, beanDefinitions, beanReferences);
    adviceDef.setSynthetic(true);

    // register the final advisor
    BeanDefinitionReaderUtils.registerWithGeneratedName(adviceDef, registry);

    return adviceDef;
  }

  /**
   * Creates the RootBeanDefinition for a POJO advice bean. Also causes pointcut
   * parsing to occur so that the pointcut may be associate with the advice bean.
   * This same pointcut is also configured as the pointcut for the enclosing
   * Advisor definition using the supplied MutablePropertyValues.
   */
  private GenericBeanDefinition createAdviceDefinition(Element adviceElement, String aspectName,
      GenericBeanDefinition methodRef ,GenericBeanDefinition aspectFactoryDef,
      List<BeanDefinition> beanDefinitions, List<RuntimeBeanReference> beanReferences) {

    GenericBeanDefinition adviceDefinition = new GenericBeanDefinition(getAdviceClass(adviceElement));
    adviceDefinition.getPropertyValues().add(new PropertyValue(ASPECT_NAME_PROPERTY, aspectName));

    ConstructorArgument cav = adviceDefinition.getConstructorArgument();
    cav.addArgumentValue(methodRef);

    Object pointcut = parsePointcutProperty(adviceElement);
    if (pointcut instanceof BeanDefinition) {
      cav.addArgumentValue(pointcut);
      beanDefinitions.add((BeanDefinition) pointcut);
    } else if (pointcut instanceof String) {
      RuntimeBeanReference pointcutRef = new RuntimeBeanReference((String) pointcut);
      cav.addArgumentValue(pointcutRef);
      beanReferences.add(pointcutRef);
    }
    // 有顺序，这里需要最后添加
    cav.addArgumentValue(aspectFactoryDef);

    return adviceDefinition;
  }

  private Object parsePointcutProperty(Element element) {
    if ((element.attribute(POINTCUT) == null) && element.attribute(POINTCUT_REF) == null) {
      return null;
    } else if (element.attribute(POINTCUT) != null) {
      String expression = element.attributeValue(POINTCUT);
      return createPointcutDefinition(expression);
    } else if (element.attribute(POINTCUT_REF) != null) {
      String pointcutRef = element.attributeValue(POINTCUT_REF);
      if (!StringUtils.hasText(pointcutRef)) {
        return null;
      }
      return pointcutRef;
    } else {
      return null;
    }
  }

  private GenericBeanDefinition createPointcutDefinition(String expression) {
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition(AspectJExpressionPointcut.class);
    beanDefinition.setScope("prototype");
    beanDefinition.setSynthetic(true);
    beanDefinition.getPropertyValues().add(new PropertyValue(EXPRESSION, expression));
    return beanDefinition;
  }

  /**
   * Gets the advice implementation class corresponding to the supplied {@link Element}.
   */
  private Class<?> getAdviceClass(Element adviceElement) {
    Class<?> resultClz;
    String elementName = adviceElement.getName();
    switch (elementName) {
      case BEFORE:
        resultClz = AspectJBeforeAdvice.class;
        break;
      case AFTER_RETURNING_ELEMENT:
        resultClz = AspectJAfterReturningAdvice.class;
        break;
      case AFTER_THROWING_ELEMENT:
        resultClz = AspectJAfterThrowingAdvice.class;
        break;
      default:
        throw new IllegalArgumentException("Unknown advice kind [" + elementName + "].");
    }
    return resultClz;
  }

  /**
  	* Return {@code true} if the supplied node describes an advice type. May be one of:
  	* '{@code before}', '{@code after}', '{@code after-returning}',
  	* '{@code after-throwing}' or '{@code around}'.
   */
  private boolean isAdviceNode(Element element) {
    String name = element.getName();
    return (BEFORE.equals(name) || AFTER.equals(name) || AFTER_RETURNING_ELEMENT.equals(name) ||
        AFTER_THROWING_ELEMENT.equals(name) || AROUND.equals(name));
  }

}
