package interpreter.model.type;

import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RefTypeTest {
    @Test
    void defaultValueIsTheNullReference() {
        assertEquals(new RefValue(0, new IntType()), new RefType(new IntType()).defaultValue());
    }

    @Test
    void nestedReferenceTypesAreComparedRecursively() {
        var refRefInt = new RefType(new RefType(new IntType()));
        assertEquals(refRefInt, new RefType(new RefType(new IntType())));
        assertNotEquals(refRefInt, new RefType(new IntType()));
    }

    @Test
    void refToIntIsDifferentFromRefToBool() {
        assertNotEquals(new RefType(new IntType()), new RefType(new BoolType()));
    }

    @Test
    void toStringShowsRefAndTheInnerType() {
        assertEquals("Ref(int)", new RefType(new IntType()).toString());
        assertEquals("Ref(Ref(int))", new RefType(new RefType(new IntType())).toString());
    }
}
