package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = TestSupport.emptyState();
        new VarDeclStmt("v", new RefType(new IntType())).execute(state);   // Ref int v
    }

    private static ValueExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    // ---------- new (S11) ----------

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void aSecondNewAllocatesAtAddressTwo() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new NewStmt("v", num(30)).execute(state);
        assertEquals(new RefValue(2, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void newOnAnUndeclaredVariableThrows() {
        assertThrows(StatementException.class, () -> new NewStmt("x", num(1)).execute(state));
    }

    @Test
    void newOnAVariableThatIsNotAReferenceThrows() throws MyException {
        new VarDeclStmt("n", new IntType()).execute(state);
        assertThrows(StatementException.class, () -> new NewStmt("n", num(1)).execute(state));
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
    }

    // ---------- wH (S12) ----------

    @Test
    void writeChangesTheHeapLocation() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        new WriteHeapStmt("v", num(30)).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void writeOnAnUndeclaredVariableThrows() {
        assertThrows(StatementException.class, () -> new WriteHeapStmt("x", num(1)).execute(state));
    }

    @Test
    void writeOnAVariableThatIsNotAReferenceThrows() throws MyException {
        new VarDeclStmt("n", new IntType()).execute(state);
        assertThrows(StatementException.class, () -> new WriteHeapStmt("n", num(1)).execute(state));
    }

    @Test
    void writeToAnAddressThatIsNotInTheHeapThrows() {
        // v is still the null reference (0, int)
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", num(1)).execute(state));
    }

    @Test
    void writeWithAValueOfTheWrongTypeThrows() throws MyException {
        new NewStmt("v", num(20)).execute(state);
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new WriteHeapStmt("v", exp).execute(state));
    }
}
