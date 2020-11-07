package cn.mghio.test.version2;

import cn.mghio.beans.propertyeditors.CustomBooleanEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class CustomBooleanEditorTestV2 {

    private CustomBooleanEditor editor = null;

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public static final String VALUE_ON = "on";
    public static final String VALUE_OFF = "off";

    public static final String VALUE_YES = "yes";
    public static final String VALUE_NO = "no";

    public static final String VALUE_1 = "1";
    public static final String VALUE_0 = "0";

    @BeforeEach
    public void beforeEach() {
        editor = new CustomBooleanEditor(true);
    }

    @Test
    public void testStringToBooleanNormal() {
        {
            editor.setAsText(VALUE_TRUE);
            assertTrue((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_ON);
            assertTrue((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_YES);
            assertTrue((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_1);
            assertTrue((Boolean) editor.getValue());
        }

        {
            editor.setAsText(VALUE_FALSE);
            assertFalse((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_OFF);
            assertFalse((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_NO);
            assertFalse((Boolean) editor.getValue());
        }
        {
            editor.setAsText(VALUE_0);
            assertFalse((Boolean) editor.getValue());
        }
    }

    @Test
    public void testEmptyStringToBoolean() {
        editor.setAsText("");
        assertNull(editor.getValue());
    }

    @Test
    public void testStringToBooleanException() {
        assertThrows(IllegalArgumentException.class, () -> {
            editor.setAsText("mghio");
            assertEquals(Boolean.class, editor.getValue());
        });
    }

}
