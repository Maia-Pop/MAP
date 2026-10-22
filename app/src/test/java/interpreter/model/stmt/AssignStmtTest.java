package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = TestSupport.emptyState();
        new VarDeclStmt("v", new IntType()).execute(state);
    }

    @Test
    void assignsTheValueOfTheExpression() throws MyException {
        new AssignStmt("v", new ValueExp(new IntValue(5))).execute(state);
        assertEquals(new IntValue(5), state.getSymTable().lookup("v"));
    }

    @Test
    void assignmentToUndeclaredVariableThrows() {
        var stmt = new AssignStmt("x", new ValueExp(new IntValue(5)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void assigningABooleanToAnIntVariableThrows() {
        var stmt = new AssignStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void errorInTheExpressionIsPropagated() {
        var stmt = new AssignStmt("v", new VarExp("undefined"));
        assertThrows(ExpressionException.class, () -> stmt.execute(state));
    }

    @Test
    void failedAssignmentLeavesTheOldValue() throws MyException {
        var stmt = new AssignStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }
}
