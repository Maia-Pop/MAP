package interpreter.model.value;

import interpreter.model.type.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValueTest {
    @Test
    void typeIsString() {
        assertEquals(new StringType(), new StringValue("a").getType());
    }

    @Test
    void equalStringsAreEqualAndHaveTheSameHashCode() {
        assertEquals(new StringValue("test.in"), new StringValue("test.in"));
        assertEquals(new StringValue("test.in").hashCode(), new StringValue("test.in").hashCode());
    }

    @Test
    void differentStringsAreNotEqual() {
        assertNotEquals(new StringValue("a"), new StringValue("b"));
    }

    @Test
    void toStringShowsTheTextInQuotes() {
        assertEquals("\"abc\"", new StringValue("abc").toString());
    }
}
