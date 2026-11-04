package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;

// evaluates the expression and adds the result to Out
public record PrintStmt(IExp exp) implements IStmt {   // S4
    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getOut().add(exp.eval(state.getSymTable()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + exp + ")";
    }
}
