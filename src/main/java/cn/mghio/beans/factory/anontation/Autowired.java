package cn.mghio.beans.factory.anontation;

import java.lang.annotation.*;

/**
 * @author mghio
 * @since 2020-11-25
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    boolean required() default true;

}
