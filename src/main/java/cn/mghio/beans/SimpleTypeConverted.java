package cn.mghio.beans;

import cn.mghio.beans.exception.TypeMismatchException;
import cn.mghio.beans.propertyeditors.CustomBooleanEditor;
import cn.mghio.beans.propertyeditors.CustomDateEditor;
import cn.mghio.beans.propertyeditors.CustomNumberEditor;
import cn.mghio.utils.ClassUtils;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mghio
 * @since 2020-11-05
 */
public class SimpleTypeConverted implements TypeConverted {

    private Map<Class<?>, PropertyEditor> defaultPropertyEditor;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SimpleTypeConverted() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {
        if (ClassUtils.isAssignable(requiredType, value)) {
            return (T) value;
        } else {
            if (value instanceof String) {
                PropertyEditor editor = this.findDefaultPropertyEditor(requiredType);
                try {
                    editor.setAsText((String) value);
                } catch (IllegalArgumentException e) {
                    throw new TypeMismatchException(value, requiredType);
                }
                return (T) editor.getValue();
            } else {
                throw new RuntimeException("Todo: can't convert value for " + value + " class:" + requiredType);
            }
        }
    }

    private PropertyEditor findDefaultPropertyEditor(Class<?> requiredType) {
        PropertyEditor editor = this.getDefaultEditor(requiredType);
        if (editor == null) {
            throw new RuntimeException("Editor for " + requiredType + " has not be implemented");
        }
        return editor;
    }

    private PropertyEditor getDefaultEditor(Class<?> requiredType) {
        if (this.defaultPropertyEditor == null) {
            createDefaultEditors();
        }
        return defaultPropertyEditor.get(requiredType);
    }

    private void createDefaultEditors() {
        this.defaultPropertyEditor = new HashMap<>(16);
        this.defaultPropertyEditor.put(boolean.class, new CustomBooleanEditor(false));
        this.defaultPropertyEditor.put(Boolean.class, new CustomBooleanEditor(true));

        this.defaultPropertyEditor.put(byte.class, new CustomNumberEditor(Byte.class, false));
        this.defaultPropertyEditor.put(Byte.class, new CustomNumberEditor(Byte.class, true));
        this.defaultPropertyEditor.put(short.class, new CustomNumberEditor(Short.class, false));
        this.defaultPropertyEditor.put(Short.class, new CustomNumberEditor(Short.class, true));
        this.defaultPropertyEditor.put(int.class, new CustomNumberEditor(Integer.class, false));
        this.defaultPropertyEditor.put(Integer.class, new CustomNumberEditor(Integer.class, true));
        this.defaultPropertyEditor.put(long.class, new CustomNumberEditor(Long.class, false));
        this.defaultPropertyEditor.put(Long.class, new CustomNumberEditor(Long.class, true));
        this.defaultPropertyEditor.put(float.class, new CustomNumberEditor(Float.class, false));
        this.defaultPropertyEditor.put(Float.class, new CustomNumberEditor(Float.class, true));
        this.defaultPropertyEditor.put(double.class, new CustomNumberEditor(Double.class, false));
        this.defaultPropertyEditor.put(Double.class, new CustomNumberEditor(Double.class, true));
        this.defaultPropertyEditor.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
        this.defaultPropertyEditor.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
        this.defaultPropertyEditor.put(Date.class, new CustomDateEditor(true, dateFormat));
    }

}
