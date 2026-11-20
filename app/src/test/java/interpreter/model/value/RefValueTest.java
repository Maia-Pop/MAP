package interpreter.model.value;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefValueTest {
    @Test
    void typeIsARefToTheLocationType() {
        assertEquals(new RefType(new IntType()), new RefValue(1, new IntType()).getType());
    }

    @Test
    void referenceToAReferenceHasANestedType() {
        var value = new RefValue(10, new RefType(new IntType()));
        assertEquals(new RefType(new RefType(new IntType())), value.getType());
    }

    @Test
    void valuesWithTheSameAddressAndTypeAreEqual() {
        assertEquals(new RefValue(1, new IntType()), new RefValue(1, new IntType()));
        assertNotEquals(new RefValue(1, new IntType()), new RefValue(2, new IntType()));
    }

    @Test
    void toStringShowsTheAddressAndTheLocationType() {
        assertEquals("(1, int)", new RefValue(1, new IntType()).toString());
    }
}
