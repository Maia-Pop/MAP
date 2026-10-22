package interpreter.model.exp;

import interpreter.exceptions.MyException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueExpTest {
    @Test
    void evaluatesToItsOwnValue() throws MyException {
        IExp e = new ValueExp(new IntValue(7));
        assertEquals(new IntValue(7), e.eval(new MyDictionary<>()));
    }
}
