package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtTest {
    private PrgState state;
    private final IStmt thenS = new AssignStmt("v", new ValueExp(new IntValue(2)));
    private final IStmt elseS = new AssignStmt("v", new ValueExp(new IntValue(3)));

    @BeforeEach
    void setUp() {
        state = TestSupport.emptyState();
    }

    @Test
    void trueConditionPushesTheThenBranch() throws MyException {
        new IfStmt(new ValueExp(new BoolValue(true)), thenS, elseS).execute(state);
        assertSame(thenS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void falseConditionPushesTheElseBranch() throws MyException {
        new IfStmt(new ValueExp(new BoolValue(false)), thenS, elseS).execute(state);
        assertSame(elseS, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void nonBooleanConditionThrows() {
        var stmt = new IfStmt(new ValueExp(new IntValue(1)), thenS, elseS);
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }
}
