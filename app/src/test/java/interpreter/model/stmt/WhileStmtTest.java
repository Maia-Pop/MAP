package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ArithExp;
import interpreter.model.exp.ArithOp;
import interpreter.model.exp.RelOp;
import interpreter.model.exp.RelationalExp;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WhileStmtTest {
    // while (v > 0) (print(v); v = v - 1)
    private static final IStmt COUNTDOWN = new WhileStmt(
            new RelationalExp(RelOp.GT, new VarExp("v"), new ValueExp(new IntValue(0))),
            new CompStmt(new PrintStmt(new VarExp("v")),
                    new AssignStmt("v", new ArithExp(ArithOp.SUB,
                            new VarExp("v"), new ValueExp(new IntValue(1))))));

    // int v; v = start; while (v > 0) (print(v); v = v - 1)   -- run until the stack is empty
    private PrgState runCountdownFrom(int start) throws MyException {
        PrgState state = PrgState.of(new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(start))), COUNTDOWN)));
        while (!state.getExeStack().isEmpty()) {
            state.getExeStack().pop().execute(state);
        }
        return state;
    }

    @Test
    void theLoopRunsZeroTimes() throws MyException {
        assertEquals("{}", runCountdownFrom(0).getOut().toString());
    }

    @Test
    void theLoopRunsOnce() throws MyException {
        assertEquals("{1}", runCountdownFrom(1).getOut().toString());
    }

    @Test
    void theLoopRunsSeveralTimes() throws MyException {
        assertEquals("{3, 2, 1}", runCountdownFrom(3).getOut().toString());
    }

    @Test
    void aTrueConditionPutsTheBodyOnTopOfTheLoop() throws MyException {
        PrgState state = TestSupport.emptyState();
        IStmt body = new NopStmt();
        IStmt loop = new WhileStmt(new ValueExp(new BoolValue(true)), body);
        loop.execute(state);

        assertSame(body, state.getExeStack().pop());
        assertSame(loop, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void aFalseConditionLeavesTheStackUnchanged() throws MyException {
        PrgState state = TestSupport.emptyState();
        new WhileStmt(new ValueExp(new BoolValue(false)), new NopStmt()).execute(state);
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void aNonBooleanConditionThrows() {
        PrgState state = TestSupport.emptyState();
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }
}
