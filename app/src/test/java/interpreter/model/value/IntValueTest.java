package interpreter.model.value;

import interpreter.model.type.IntType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntValueTest {
    @Test
    void typeIsInt() {
        assertEquals(new IntType(), new IntValue(3).getType());
    }

    @Test
    void valuesWithTheSameNumberAreEqual() {
        assertEquals(new IntValue(3), new IntValue(3));
        assertNotEquals(new IntValue(3), new IntValue(4));
    }

    @Test
    void toStringShowsTheNumber() {
        assertEquals("3", new IntValue(3).toString());
    }
}
