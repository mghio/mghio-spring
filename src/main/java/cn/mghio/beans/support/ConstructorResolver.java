package cn.mghio.beans.support;

import cn.mghio.beans.BeanDefinition;
import cn.mghio.beans.ConstructorArgument;
import cn.mghio.beans.SimpleTypeConverted;
import cn.mghio.beans.exception.BeanCreationException;
import cn.mghio.beans.factory.config.ConfigurableBeanFactory;
import cn.mghio.beans.factory.support.DefaultBeanFactory;
import cn.mghio.utils.ClassUtils;
import cn.mghio.utils.StringUtils;
import lombok.AllArgsConstructor;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author mghio
 * @since 2020-11-07
 */
@AllArgsConstructor
public class ConstructorResolver {

    private final DefaultBeanFactory beanFactory;

    public Object autowireConstructor(BeanDefinition bd) {
        Constructor<?> constructorToUse = null;
        Object[] argumentToUse = null;

        Class<?> beanClass = null;
        try {
            beanClass = this.beanFactory.getClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(bd.getId(), "Instantiation of bean failed, can't resolve constructor");
        }

        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        ConstructorArgument constructorArgument = bd.getConstructorArgument();
        SimpleTypeConverted typeConverted = new SimpleTypeConverted();

        for (Constructor<?> candidate : candidates) {
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            boolean valid = preMatchCheck(constructorArgument, parameterTypes);
            if (!valid) {
                continue;
            }
            argumentToUse = new Object[parameterTypes.length];

            boolean readyFound = valuesMatchTypes(argumentToUse, valueResolver, constructorArgument,
                    typeConverted, parameterTypes);

            if (readyFound) {
                constructorToUse = candidate;
                break;
            }
        }

        if (constructorToUse == null) {
            throw new BeanCreationException(bd.getId(), "can't find a approaching constructor");
        }

        try {
            return constructorToUse.newInstance(argumentToUse);
        } catch (Exception e) {
            throw new BeanCreationException(bd.getId(), "can't find a create instance using " + constructorToUse);
        }
    }

    private boolean preMatchCheck(ConstructorArgument constructorArgument, Class<?>[] parameterTypes) {
        try {
            if (parameterTypes.length != constructorArgument.getArgumentCount()) {
                return false;
            }
            List<ConstructorArgument.ValueHolder> argumentsValues = constructorArgument.getArgumentsValues();
            for (int i = 0; i < parameterTypes.length; i++) {
                String argTypeName = argumentsValues.get(i).getType();
                ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
                if (StringUtils.hasLength(argTypeName) && !ClassUtils.isAssignable(classLoader.loadClass(argTypeName), parameterTypes[i])) {
                    return false;
                }
            }
            return true;
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException("type class not found exception", e);
        }
    }

    private boolean valuesMatchTypes(Object[] argumentToUse, BeanDefinitionValueResolver valueResolver,
                                     ConstructorArgument constructorArgument, SimpleTypeConverted typeConverted,
                                     Class<?>[] parameterTypes) {
        try {
            for (int i = 0; i < parameterTypes.length; i++) {
                ConstructorArgument.ValueHolder valueHolder = constructorArgument.getArgumentsValues().get(i);
                Object originalValue = valueHolder.getValue(); // TypedStringValue or RuntimeBeanReference
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                Object convertedValue = typeConverted.convertIfNecessary(resolvedValue, parameterTypes[i]);
                argumentToUse[i] = convertedValue;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
