package cn.mghio.stereotype;

import java.lang.annotation.*;

/**
 * @author mghio
 * @since 2020-11-25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

    String value() default "";

}
