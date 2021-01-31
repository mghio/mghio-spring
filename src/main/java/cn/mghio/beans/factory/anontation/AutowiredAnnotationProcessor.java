package cn.mghio.beans.factory.anontation;

import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.exception.BeansException;
import cn.mghio.beans.factory.config.AutowireCapableBeanFactory;
import cn.mghio.beans.factory.config.InstantiationAwareBeanProcessor;
import cn.mghio.core.annotation.AnnotationUtils;
import cn.mghio.utils.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class AutowiredAnnotationProcessor implements InstantiationAwareBeanProcessor {

  private AutowireCapableBeanFactory beanFactory;

  private final String requiredParameterName = "required";

  private boolean requiredParameterValue = true;

  private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>();

  public AutowiredAnnotationProcessor() {
    this.autowiredAnnotationTypes.add(Autowired.class);
  }

  public InjectionMetadata buildAutowiringMetadata(Class<?> clz) {
    LinkedList<InjectionElement> elements = new LinkedList<>();
    Class<?> targetClass = clz;

    do {
      LinkedList<InjectionElement> currElements = new LinkedList<>();
      for (Field field : targetClass.getDeclaredFields()) {
        Annotation ann = findAutowiredAnnotation(field);
        if (ann != null) {
          if (Modifier.isStatic(field.getModifiers())) {
            continue;
          }

          boolean required = determineRequiredStatus(ann);
          elements.add(new AutowiredFieldElement(field, required, beanFactory));
        }
      }

      for (Method method : targetClass.getDeclaredMethods()) {
        // TODO @mghio handle method autowired
      }

      elements.addAll(0, currElements);
      targetClass = targetClass.getSuperclass();
    } while (targetClass != null && targetClass != Object.class);
    return new InjectionMetadata(clz, elements);
  }

  public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  protected boolean determineRequiredStatus(Annotation ann) {
    try {
      Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
      if (method == null) {
        return true;
      }
      return (this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, ann));
    } catch (Exception e) {
      return true;
    }
  }

  private Annotation findAutowiredAnnotation(AccessibleObject ao) {
    for (Class<? extends Annotation> annotationType : this.autowiredAnnotationTypes) {
      Annotation ann = AnnotationUtils.getAnnotation(ao, annotationType);
      if (ann != null) {
        return ann;
      }
    }
    return null;
  }

  @Override
  public Object beforeInitialization(Object bean, String beanName) throws BeansException {
    // do nothing
    return bean;
  }

  @Override
  public Object afterInitialization(Object bean, String beanName) throws BeansException {
    // do nothing
    return bean;
  }

  @Override
  public Object beforeInstantiation(Class<?> beanClz, String beanName) throws BeansException {
    return null;
  }

  @Override
  public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
    // do nothing
    return true;
  }

  @Override
  public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
    InjectionMetadata metadata = this.buildAutowiringMetadata(bean.getClass());
    try {
      metadata.inject(bean);
    } catch (Throwable e) {
      throw new BeanCreationException(beanName, "Injection of autowired dependencies failed");
    }
  }
}
