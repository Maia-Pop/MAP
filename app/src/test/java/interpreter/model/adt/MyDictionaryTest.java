package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String, Integer> dict;

    @BeforeEach
    void setUp() {
        dict = new MyDictionary<>();
    }

    @Test
    void putThenLookupReturnsTheValue() throws ADTException {
        dict.put("a", 5);
        assertTrue(dict.isDefined("a"));
        assertEquals(5, dict.lookup("a"));
    }

    @Test
    void keyThatWasNeverAddedIsNotDefined() {
        assertFalse(dict.isDefined("a"));
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        dict.put("a", 5);
        dict.update("a", 7);
        assertEquals(7, dict.lookup("a"));
    }

    @Test
    void lookupOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.lookup("missing"));
    }

    @Test
    void updateOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.update("missing", 1));
    }

    @Test
    void toStringShowsKeyArrowValue() {
        dict.put("v", 2);
        assertEquals("{v -> 2}", dict.toString());
    }
}
