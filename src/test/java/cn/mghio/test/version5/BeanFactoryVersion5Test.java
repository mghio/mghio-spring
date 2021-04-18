package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cn.mghio.aop.Advice;
import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJAfterThrowingAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.beans.factory.BeanFactory;
import cn.mghio.tx.TransactionManager;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class BeanFactoryVersion5Test extends AbstractVersion5Test {

  private static final String EXPECTED_EXPRESSION = "execution(* cn.mghio.service.version5.*.placeOrder(..))";

  @Test
  public void testGetBeanByType() throws Exception {
    BeanFactory factory = getBeanFactory("orderservice-version5.xml");
    List<Object> advices = factory.getBeansByType(Advice.class);
    assertEquals(3, advices.size());

    {
      AspectJBeforeAdvice advice = (AspectJBeforeAdvice) getAdvice(AspectJBeforeAdvice.class, advices);
      assertNotNull(advice);
      assertEquals(TransactionManager.class.getMethod("start"), advice.getAdviceMethod());
      assertEquals(EXPECTED_EXPRESSION, advice.getPointcut().getExpression());
      assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
    }

    {
      AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice) getAdvice(AspectJAfterReturningAdvice.class, advices);
      assertNotNull(advice);
      assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());
      assertEquals(EXPECTED_EXPRESSION, advice.getPointcut().getExpression());
      assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
    }

    {
      AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice) getAdvice(AspectJAfterThrowingAdvice.class, advices);
      assertNotNull(advice);
      assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());
      assertEquals(EXPECTED_EXPRESSION, advice.getPointcut().getExpression());
      assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());
    }
  }

  private Object getAdvice(Class<?> type, List<Object> advices) {
    for (Object advice : advices) {
      if (advice.getClass().equals(type)) {
        return advice;
      }
    }
    return null;
  }

}
