package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NopStmtTest {
    @Test
    void doesNotChangeTheState() throws MyException {
        PrgState state = TestSupport.emptyState();
        PrgState result = new NopStmt().execute(state);

        assertSame(state, result);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(0, state.getOut().size());
        assertEquals("{}", state.getSymTable().toString());
    }
}
