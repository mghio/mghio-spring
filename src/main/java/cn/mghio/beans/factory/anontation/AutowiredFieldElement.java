package cn.mghio.beans.factory.anontation;

import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.config.AutowireCapableBeanFactory;
import cn.mghio.beans.factory.config.DependencyDescriptor;
import cn.mghio.utils.ReflectionUtils;
import java.lang.reflect.Field;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class AutowiredFieldElement extends InjectionElement {

  private final boolean required;

  public AutowiredFieldElement(Field field, boolean required, AutowireCapableBeanFactory factory) {
    super(field, factory);
    this.required = required;
  }

  public Field getField() {
    return (Field) this.member;
  }

  @Override
  void inject(Object target) {
    Field field = this.getField();
    try {
      DependencyDescriptor descriptor = new DependencyDescriptor(field, this.required);
      Object value = factory.resolveDependency(descriptor);
      if (value != null) {
        ReflectionUtils.makeAccessible(field);
        field.set(target, value);
      }
    } catch (Throwable e) {
      throw new BeanCreationException("Could not autowire field:" + field, e);
    }
  }
}
