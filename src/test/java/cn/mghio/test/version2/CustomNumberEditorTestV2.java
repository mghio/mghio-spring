package cn.mghio.test.version2;

import cn.mghio.beans.propertyeditors.CustomNumberEditor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class CustomNumberEditorTestV2 {

    private CustomNumberEditor editor = null;

    @BeforeEach
    public void beforeEach() {
        editor = new CustomNumberEditor(Number.class, true);
    }

    @Test
    public void testStringToNumberNormal() {
        {
            editor.setAsText("1");
            Object value = editor.getValue();
            assertTrue(value instanceof Integer);
            assertEquals(1, ((Integer) value).intValue());
        }
        {
            editor.setAsText("");
            Object value = editor.getValue();
            assertNull(value);
        }
    }

    @Test
    public void testEmptyStringToNumberException() {
        Assertions.assertThrows(NumberFormatException.class, () -> {
            editor.setAsText("1.1");
            editor.getValue();
        });
    }
}
