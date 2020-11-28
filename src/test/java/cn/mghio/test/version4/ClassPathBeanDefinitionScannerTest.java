package cn.mghio.test.version4;

import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.context.annotation.ClassPathBeanDefinitionScanner;
import org.junit.jupiter.api.Test;

/**
 * @author mghio
 * @since 2020-11-28
 */
public class ClassPathBeanDefinitionScannerTest extends AbstractBeanDefinitionTest {

    @Test
    public void testScanAndRegistry() {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        String basePackages = "cn.mghio.dao.version4,cn.mghio.service.version4";
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.doScanAndRegistry(basePackages);

        super.testComponentAnnotation(beanFactory);
    }

}
