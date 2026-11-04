package interpreter.model.stmt;

import interpreter.model.PrgState;

// pushes the second statement, then the first, so the first ends up on top and runs next
public record CompStmt(IStmt first, IStmt second) implements IStmt {   // S1
    @Override
    public PrgState execute(PrgState state) {
        state.getExeStack().push(second);
        state.getExeStack().push(first);
        return state;
    }

    @Override
    public String toString() {
        return "(" + first + "; " + second + ")";
    }
}
