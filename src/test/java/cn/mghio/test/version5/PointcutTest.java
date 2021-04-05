package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.mghio.aop.MethodMatcher;
import cn.mghio.aop.aspectj.AspectJExpressionPointcut;
import cn.mghio.service.version5.OrderService;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-04
 */
public class PointcutTest {

  @Test
  public void testPointcut() throws Exception {
    String expression = "execution(* cn.mghio.service.version5.*.placeOrder(..))";

    AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
    pc.setExpression(expression);

    MethodMatcher methodMatcher = pc.getMethodMatcher();
    {
      Class<?> targetClass = OrderService.class;
      Method m1 = targetClass.getMethod("placeOrder");
      assertTrue(methodMatcher.matches(m1));

      Method m2 = targetClass.getMethod("getStockDao");
      assertFalse(methodMatcher.matches(m2));
    }

    {
      Class<?> targetClass = cn.mghio.service.version4.OrderService.class;
      Method m1 = targetClass.getMethod("getStockDao");
      assertFalse(methodMatcher.matches(m1));

      Method m2 = targetClass.getMethod("getTradeDao");
      assertFalse(methodMatcher.matches(m2));
    }
  }

}
