package cn.mghio.test.version5;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.mghio.aop.aspectj.AspectJAfterReturningAdvice;
import cn.mghio.aop.aspectj.AspectJAfterThrowingAdvice;
import cn.mghio.aop.aspectj.AspectJBeforeAdvice;
import cn.mghio.aop.aspectj.AspectJExpressionPointcut;
import cn.mghio.aop.config.AopInstanceFactory;
import cn.mghio.aop.config.MethodLocatingFactory;
import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.ConstructorArgument.ValueHolder;
import cn.mghio.beans.PropertyValue;
import cn.mghio.beans.RuntimeBeanReference;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.tx.TransactionManager;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2021-04-18
 */
public class BeanDefinitionVersion5Test extends AbstractVersion5Test {

  private static final String TARGET_BEAN_NAME = "targetBeanName";

  private static final String METHOD_NAME = "methodName";

  private static final String ASPECT_BEAN_NAME = "aspectBeanName";

  @Test
  public void testAopBean() {
    DefaultBeanFactory factory = (DefaultBeanFactory) getBeanFactory("orderservice-version5.xml");

    // 检查名称为 tx 的 bean 定义是否生成
    {
      BeanDefinition bd = factory.getBeanDefinition("tx");
      assertEquals(TransactionManager.class.getName(), bd.getBeanClassName());
    }

    // 检查 placeOrder 是否正确生成
    {
      BeanDefinition bd = factory.getBeanDefinition("placeOrder");
      // 合成的 bean
      assertTrue(bd.isSynthetic());
      assertEquals(AspectJExpressionPointcut.class, bd.getBeanClass());

      PropertyValue pv = bd.getPropertyValues().get(0);
      assertEquals("expression", pv.getName());
      assertEquals("execution(* cn.mghio.service.version5.*.placeOrder(..))", pv.getValue());
    }

    // 检查 AspectJBeforeAdvice
    assertAspectJBeforeAdvice(factory);

    // 检查 AspectJAfterReturningAdvice
    assertAspectJAfterReturningAdvice(factory);

    // 检查 AspectJAfterThrowingAdvice
    assertAspectJAfterThrowingAdvice(factory);
  }

  /**
   * 检查 AspectJAfterThrowingAdvice
   */
  private void assertAspectJAfterThrowingAdvice(DefaultBeanFactory factory) {
    String name = AspectJAfterThrowingAdvice.class.getName() + "#0";
    BeanDefinition bd = factory.getBeanDefinition(name);
    assertEquals(AspectJAfterThrowingAdvice.class, bd.getBeanClass());
    // 合成的 bean
    assertTrue(bd.isSynthetic());

    List<ValueHolder> args = bd.getConstructorArgument().getArgumentsValues();
    assertEquals(3, args.size());

    // 构造第一个参数
    {
      BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
      // 合成的 bean
      assertTrue(bd.isSynthetic());
      assertEquals(MethodLocatingFactory.class, innerBeanDef.getBeanClass());

      List<PropertyValue> propertyValues = innerBeanDef.getPropertyValues();
      assertEquals(2, propertyValues.size());
      assertEquals(TARGET_BEAN_NAME, propertyValues.get(0).getName());
      assertEquals("tx", propertyValues.get(0).getValue());
      assertEquals(METHOD_NAME, propertyValues.get(1).getName());
      assertEquals("rollback", propertyValues.get(1).getValue());
    }

    // 构造的第二个参数
    {
      RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
      assertEquals("placeOrder", ref.getBeanName());
    }

    // 构造的第三个参数
    {
      BeanDefinition innerBeanRef = (BeanDefinition) args.get(2).getValue();
      // 合成的 bean
      assertTrue(innerBeanRef.isSynthetic());
      assertEquals(AopInstanceFactory.class, innerBeanRef.getBeanClass());

      List<PropertyValue> propertyValues = innerBeanRef.getPropertyValues();
      assertEquals(1, propertyValues.size());
      assertEquals(ASPECT_BEAN_NAME, propertyValues.get(0).getName());
      assertEquals("tx", propertyValues.get(0).getValue());
    }
  }

  /**
   * 检查 AspectJAfterReturningAdvice
   */
  private void assertAspectJAfterReturningAdvice(DefaultBeanFactory factory) {
    String name = AspectJAfterReturningAdvice.class.getName() + "#0";
    BeanDefinition bd = factory.getBeanDefinition(name);
    assertEquals(AspectJAfterReturningAdvice.class, bd.getBeanClass());
    // 合成的 bean
    assertTrue(bd.isSynthetic());

    List<ValueHolder> args = bd.getConstructorArgument().getArgumentsValues();
    assertEquals(3, args.size());

    // 构造第一个参数
    {
      BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
      // 合成的 bean
      assertTrue(bd.isSynthetic());
      assertEquals(MethodLocatingFactory.class, innerBeanDef.getBeanClass());

      List<PropertyValue> propertyValues = innerBeanDef.getPropertyValues();
      assertEquals(2, propertyValues.size());
      assertEquals(TARGET_BEAN_NAME, propertyValues.get(0).getName());
      assertEquals("tx", propertyValues.get(0).getValue());
      assertEquals(METHOD_NAME, propertyValues.get(1).getName());
      assertEquals("commit", propertyValues.get(1).getValue());
    }

    // 构造的第二个参数
    {
      RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
      assertEquals("placeOrder", ref.getBeanName());
    }

    // 构造的第三个参数
    {
      BeanDefinition innerBeanRef = (BeanDefinition) args.get(2).getValue();
      // 合成的 bean
      assertTrue(innerBeanRef.isSynthetic());
      assertEquals(AopInstanceFactory.class, innerBeanRef.getBeanClass());

      List<PropertyValue> propertyValues = innerBeanRef.getPropertyValues();
      assertEquals(1, propertyValues.size());
      assertEquals(ASPECT_BEAN_NAME, propertyValues.get(0).getName());
      assertEquals("tx", propertyValues.get(0).getValue());
    }
  }

  /**
   * 检查 AspectJBeforeAdvice
   */
  private void assertAspectJBeforeAdvice(DefaultBeanFactory factory) {
    {
      String name = AspectJBeforeAdvice.class.getName() + "#0";
      BeanDefinition bd = factory.getBeanDefinition(name);
      assertEquals(AspectJBeforeAdvice.class, bd.getBeanClass());
      // 合成的 bean
      assertTrue(bd.isSynthetic());

      List<ValueHolder> args = bd.getConstructorArgument().getArgumentsValues();
      assertEquals(3, args.size());

      // 构造的第一个参数
      {
        BeanDefinition innerBeanDef = (BeanDefinition) args.get(0).getValue();
        // 合成的 bean
        assertTrue(innerBeanDef.isSynthetic());
        assertEquals(MethodLocatingFactory.class, innerBeanDef.getBeanClass());

        List<PropertyValue> propertyValues = innerBeanDef.getPropertyValues();
        assertEquals(2, propertyValues.size());
        assertEquals(TARGET_BEAN_NAME, propertyValues.get(0).getName());
        assertEquals("tx", propertyValues.get(0).getValue());
        assertEquals(METHOD_NAME, propertyValues.get(1).getName());
        assertEquals("start", propertyValues.get(1).getValue());
      }

      // 构造的第二个参数
      {
        RuntimeBeanReference ref = (RuntimeBeanReference) args.get(1).getValue();
        assertEquals("placeOrder", ref. getBeanName());
      }

      // 构造的第三个参数
      {
        BeanDefinition innerBeanDef = (BeanDefinition) args.get(2).getValue();
        // 合成的 bean
        assertTrue(innerBeanDef.isSynthetic());
        assertEquals(AopInstanceFactory.class, innerBeanDef.getBeanClass());

        List<PropertyValue> propertyValues = innerBeanDef.getPropertyValues();
        assertEquals(1, propertyValues.size());
        assertEquals(ASPECT_BEAN_NAME, propertyValues.get(0).getName());
        assertEquals("tx", propertyValues.get(0).getValue());
      }
    }
  }

}
