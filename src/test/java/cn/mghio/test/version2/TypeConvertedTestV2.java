package cn.mghio.test.version2;

import cn.mghio.beans.SimpleTypeConverted;
import cn.mghio.beans.TypeConverted;
import cn.mghio.beans.exception.TypeMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author mghio
 * @since 2020-11-07
 */
public class TypeConvertedTestV2 {

    private TypeConverted typeConverted = null;

    @BeforeEach
    public void beforeEach() {
        typeConverted = new SimpleTypeConverted();
    }

    @Test
    public void testStringToIntNormal() {
        Integer value = typeConverted.convertIfNecessary("3", Integer.class);
        assertEquals(3, value.intValue());
    }

    @Test
    public void testStringToBooleanNormal() {
        {
            Boolean value = typeConverted.convertIfNecessary("1", Boolean.class);
            assertTrue(value);
        }
        {
            Boolean value = typeConverted.convertIfNecessary("0", Boolean.class);
            assertFalse(value);
        }
        {
            Boolean value = typeConverted.convertIfNecessary("true", Boolean.class);
            assertTrue(value);
        }
        {
            Boolean value = typeConverted.convertIfNecessary("false", Boolean.class);
            assertFalse(value);
        }
    }

    @Test
    public void testStringToIntException() {
        assertThrows(TypeMismatchException.class, () -> typeConverted.convertIfNecessary("4.9", Integer.class));
    }

    @Test
    public void testStringToBooleanException() {
        assertThrows(TypeMismatchException.class, () -> typeConverted.convertIfNecessary("mghio", Boolean.class));
    }

}
