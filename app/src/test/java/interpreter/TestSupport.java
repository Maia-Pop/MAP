package interpreter;

import interpreter.exceptions.ADTException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.NopStmt;

/** Helper for the tests: builds fresh program states. */
public final class TestSupport {
    private TestSupport() {
    }

    /** A state with an empty ExeStack, an empty SymTable and an empty Out. */
    public static PrgState emptyState() {
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
        try {
            state.getExeStack().pop();   // remove the initial program
        } catch (ADTException e) {
            throw new IllegalStateException(e);
        }
        return state;
    }
}
