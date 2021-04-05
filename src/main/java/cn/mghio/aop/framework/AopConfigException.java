package cn.mghio.aop.framework;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AopConfigException extends RuntimeException {

  public AopConfigException(String msg) {
    super(msg);
  }

  public AopConfigException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
