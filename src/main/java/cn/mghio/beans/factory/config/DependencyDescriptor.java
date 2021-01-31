package cn.mghio.beans.factory.config;

import cn.mghio.utils.Assert;
import java.lang.reflect.Field;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class DependencyDescriptor {

  private Field field;
  private boolean required;

  public DependencyDescriptor(Field field, boolean required) {
    Assert.notNull(field, "Field must not be null");
    this.field = field;
    this.required = required;
  }

  public Class<?> getDependencyType() {
    if (this.field != null) {
      return field.getType();
    }

    // TODO @mghio constructor and setter autowire

    throw new RuntimeException("only support field dependency");
  }

  public boolean isRequired() {
    return this.required;
  }
}
