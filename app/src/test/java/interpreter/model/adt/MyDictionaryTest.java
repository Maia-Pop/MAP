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

    @Test
    void removeDeletesTheKey() throws ADTException {
        dict.put("a", 5);
        dict.remove("a");
        assertFalse(dict.isDefined("a"));
    }

    @Test
    void removeOfMissingKeyThrows() {
        assertThrows(ADTException.class, () -> dict.remove("missing"));
    }

    @Test
    void toMapReturnsTheEntries() {
        dict.put("a", 5);
        assertEquals(1, dict.toMap().size());
        assertEquals(5, dict.toMap().get("a"));
    }
}
