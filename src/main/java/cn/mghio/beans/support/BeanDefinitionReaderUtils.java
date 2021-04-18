package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.exception.BeanDefinitionStoreException;
import cn.mghio.utils.StringUtils;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class BeanDefinitionReaderUtils {

  public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

  public static String generateBeanName(
      BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
      throws BeanDefinitionStoreException {

    String generatedBeanName = definition.getBeanClassName();
    if (!StringUtils.hasText(generatedBeanName)) {
      throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither " +
          "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
    }

    String id = generatedBeanName;
    if (isInnerBean) {
      // Inner bean: generate identity hashcode suffix.
      id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR +
          Integer.toHexString(System.identityHashCode(definition));
    } else {
      // Top-level bean: use plain class name.
      // Increase counter until the id is unique.
      int counter = -1;
      while (counter == -1 || registry.getBeanDefinition(id) != null) {
        counter++;
        id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
      }
    }
    return id;
  }

  public static String generateBeanName(BeanDefinition beanDefinition,
      BeanDefinitionRegistry registry)
      throws BeanDefinitionStoreException {

    return generateBeanName(beanDefinition, registry, false);
  }

  public static String registerWithGeneratedName(
      GenericBeanDefinition definition, BeanDefinitionRegistry registry)
      throws BeanDefinitionStoreException {

    String generatedName = generateBeanName(definition, registry, false);
    registry.registerBeanDefinition(generatedName, definition);
    return generatedName;
  }

}
