package cn.mghio.beans.factory.support;

import cn.mghio.beans.factory.config.SingletonBeanRegistry;
import cn.mghio.utils.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>(64);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName, "'beanName' must not be null");
        Object oldObject = this.singletonObjects.get(beanName);
        if (null != oldObject) {
            throw new IllegalArgumentException("Could not register object [" + singletonObject + "]" +
                    "under bean name '" + beanName + "' there is already object [" + oldObject + "]");
        }
        this.singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }
}
