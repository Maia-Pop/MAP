package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {
    @Test
    void appendsTheValueToOut() throws MyException {
        PrgState state = TestSupport.emptyState();
        new PrintStmt(new ValueExp(new IntValue(1))).execute(state);
        new PrintStmt(new ValueExp(new IntValue(2))).execute(state);

        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getOut().get(1));
    }

    @Test
    void errorInTheExpressionIsPropagated() {
        PrgState state = TestSupport.emptyState();
        assertThrows(ExpressionException.class,
                () -> new PrintStmt(new VarExp("x")).execute(state));
    }
}
