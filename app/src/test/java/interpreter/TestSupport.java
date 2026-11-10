package interpreter;

import interpreter.exceptions.ADTException;
import interpreter.model.PrgState;
import interpreter.model.stmt.NopStmt;

/** Helper for the tests: builds fresh program states. */
public final class TestSupport {
    private TestSupport() {
    }

    /** A state with an empty ExeStack, an empty SymTable, an empty Out and an empty FileTable. */
    public static PrgState emptyState() {
        PrgState state = PrgState.of(new NopStmt());
        try {
            state.getExeStack().pop();   // remove the initial program
        } catch (ADTException e) {
            throw new IllegalStateException(e);
        }
        return state;
    }
}
