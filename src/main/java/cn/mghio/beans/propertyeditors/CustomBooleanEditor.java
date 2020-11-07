package cn.mghio.beans.propertyeditors;

import cn.mghio.utils.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author mghio
 * @since 2020-11-06
 */
public class CustomBooleanEditor extends PropertyEditorSupport {

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";

    public static final String VALUE_YES = "yes";
    public static final String VALUE_NO = "no";

    public static final String VALUE_1 = "1";
    public static final String VALUE_0 = "0";

    private final boolean allowEmpty;

    public CustomBooleanEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String input = (text != null) ? text.trim() : null;
        if (this.allowEmpty && !StringUtils.hasText(input)) {
            setValue(null);
        } else if (VALUE_TRUE.equalsIgnoreCase(input) || VALUE_ON.equalsIgnoreCase(input) ||
                VALUE_YES.equalsIgnoreCase(input) || VALUE_1.equalsIgnoreCase(input)) {
            setValue(Boolean.TRUE);
        } else if (VALUE_FALSE.equalsIgnoreCase(input) || VALUE_OFF.equalsIgnoreCase(input) ||
                VALUE_NO.equalsIgnoreCase(input) || VALUE_0.equalsIgnoreCase(input)) {
            setValue(Boolean.FALSE);
        } else {
            throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (Boolean.TRUE.equals(value)) {
            return VALUE_TRUE;
        } else if (Boolean.FALSE.equals(value)) {
            return VALUE_FALSE;
        }
        return "";
    }
}
