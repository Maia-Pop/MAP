package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {
    @Test
    void pushesSecondThenFirstSoFirstIsOnTop() throws MyException {
        PrgState state = TestSupport.emptyState();
        IStmt first = new NopStmt();
        IStmt second = new PrintStmtForTest();
        new CompStmt(first, second).execute(state);

        assertSame(first, state.getExeStack().pop());
        assertSame(second, state.getExeStack().pop());
        assertTrue(state.getExeStack().isEmpty());
    }

    // a statement different from NopStmt, so that assertSame can tell them apart
    private record PrintStmtForTest() implements IStmt {
        @Override
        public PrgState execute(PrgState state) {
            return state;
        }
    }
}
