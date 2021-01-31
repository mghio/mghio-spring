package cn.mghio.beans.factory.anontation;

import cn.mghio.beans.factory.config.AutowireCapableBeanFactory;
import java.lang.reflect.Member;

/**
 * @author mghio
 * @since 2021-01-30
 */
public abstract class InjectionElement {

  protected Member member;

  protected AutowireCapableBeanFactory factory;

  public InjectionElement(Member member, AutowireCapableBeanFactory factory) {
    this.member = member;
    this.factory = factory;
  }

  abstract void inject(Object target);
}
