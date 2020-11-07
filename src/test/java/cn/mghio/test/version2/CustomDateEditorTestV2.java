package cn.mghio.test.version2;

import cn.mghio.beans.propertyeditors.CustomDateEditor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class CustomDateEditorTestV2 {

    private CustomDateEditor editor = null;

    private DateFormat dateFormat = null;

    @BeforeEach
    public void beforeEach() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        editor = new CustomDateEditor(true, dateFormat);
    }

    @Test
    public void testStringToDate() {
        editor.setAsText("2020-11-07 12:21:21");
        Object value = editor.getValue();
        assertTrue(value instanceof Date);
    }

    @Test
    public void testStringToDateExactDateLengthException() {
        assertThrows(IllegalArgumentException.class, () -> {
            editor = new CustomDateEditor(true, dateFormat, 10);
            editor.setAsText("2020-11-07 12:21:21");
        });
    }

    @Test
    public void testStringToDateIllegalException() {
        assertThrows(IllegalArgumentException.class, () -> {
            editor.setAsText("2020-11-0");
        });
    }

}
