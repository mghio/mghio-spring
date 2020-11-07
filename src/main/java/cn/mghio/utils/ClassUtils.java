package cn.mghio.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Copy from springframework.
 *
 * @author mghio
 * @since 2020-10-31
 */
public class ClassUtils {

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);

    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static boolean isAssignable(Class<?> type, Object value) {
        Assert.notNull(type,"Type must not be null");
        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        Assert.notNull(lhsType, "Left-hand side must not be null");
        Assert.notNull(rhsType, "Right-hand side must not be null");
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        } else if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            return lhsType.equals(resolvedPrimitive);
        } else {
            Class<?> resolvedWrapper = primitiveWrapperTypeMap.get(rhsType);
            return resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper);
        }
    }

}
