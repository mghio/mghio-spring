package cn.mghio.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author mghio
 * @since 2021-01-30
 */
public class AnnotationUtils {

  public static <T extends Annotation> T getAnnotation(AnnotatedElement ae, Class<T> annotationType) {
    T ann = ae.getAnnotation(annotationType);
    if (ann == null) {
      for (Annotation metaAnn : ae.getAnnotations()) {
        ann = metaAnn.annotationType().getAnnotation(annotationType);
        if (ann != null) {
          break;
        }
      }
    }
    return ann;
  }

}
