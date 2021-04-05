package cn.mghio.aop.cglib.core;

import net.sf.cglib.core.DefaultNamingPolicy;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class SpringNamingPolicy extends DefaultNamingPolicy {

  public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();

  @Override
  protected String getTag() {
    return "BySpringCGLIB";
  }

}
