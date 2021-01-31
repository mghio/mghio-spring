package cn.mghio.beans.factory.anontation;

import java.util.List;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class InjectionMetadata {

  private final Class<?> targetClass;

  private List<InjectionElement> injectionElements;

  public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElements) {
    this.targetClass = targetClass;
    this.injectionElements = injectionElements;
  }

  public List<InjectionElement> getInjectionElements() {
    return injectionElements;
  }

  public void inject(Object target) {
    if (injectionElements == null || injectionElements.isEmpty()) {
      return;
    }

    for (InjectionElement element : injectionElements) {
      element.inject(target);
    }
  }
}
