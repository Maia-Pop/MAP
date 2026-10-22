package interpreter.model.value;

import interpreter.model.type.BoolType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoolValueTest {
    @Test
    void typeIsBool() {
        assertEquals(new BoolType(), new BoolValue(true).getType());
    }

    @Test
    void valuesWithTheSameBooleanAreEqual() {
        assertEquals(new BoolValue(true), new BoolValue(true));
        assertNotEquals(new BoolValue(true), new BoolValue(false));
    }

    @Test
    void toStringShowsTrueOrFalse() {
        assertEquals("true", new BoolValue(true).toString());
    }
}
