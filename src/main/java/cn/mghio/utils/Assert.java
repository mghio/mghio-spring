package cn.mghio.utils;

/**
 * @author mghio
 * @since 2020-11-01
 */
public class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
