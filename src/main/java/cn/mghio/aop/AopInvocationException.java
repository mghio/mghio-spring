package cn.mghio.aop;

/**
 * @author mghio
 * @since 2021-04-05
 */
public class AopInvocationException extends RuntimeException {

  public AopInvocationException(String msg) {
    super(msg);
  }

  public AopInvocationException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
