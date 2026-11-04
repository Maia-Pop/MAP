package interpreter.model.stmt;

import interpreter.model.PrgState;

// does nothing
public record NopStmt() implements IStmt {   // S6
    @Override
    public PrgState execute(PrgState state) {
        return state;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
