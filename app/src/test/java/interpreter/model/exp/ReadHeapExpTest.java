package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadHeapExpTest {
    private final IDictionary<String, IValue> symTable = new MyDictionary<>();
    private final IHeap heap = new MyHeap();

    @Test
    void readsTheValueAtTheAddress() throws MyException {
        heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(1, new IntType()));
        assertEquals(new IntValue(20), new ReadHeapExp(new VarExp("v")).eval(symTable, heap));
    }

    @Test
    void readsThroughTwoLevelsOfReferences() throws MyException {
        heap.allocate(new IntValue(20));                       // 1 -> 20
        heap.allocate(new RefValue(1, new IntType()));         // 2 -> (1, int)
        symTable.put("a", new RefValue(2, new RefType(new IntType())));
        IExp e = new ReadHeapExp(new ReadHeapExp(new VarExp("a")));
        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void anExpressionThatIsNotAReferenceThrows() {
        IExp e = new ReadHeapExp(new ValueExp(new IntValue(1)));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void anAddressThatIsNotInTheHeapThrows() {
        symTable.put("v", new RefValue(5, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
        assertEquals("rH: address 5 is not in the heap", ex.getMessage());
    }

    @Test
    void theNullReferenceThrows() {
        symTable.put("v", new RefValue(0, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }
}
