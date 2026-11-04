package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.BoolValue;

// evaluates the condition, which must be a BoolValue or it throws
// it then pushes the then-branch or the else-branch on the stack
// (it doesn’t run the branch itself)
public record IfStmt(IExp cond, IStmt thenS, IStmt elseS) implements IStmt {   // S5
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (!(cond.eval(state.getSymTable()) instanceof BoolValue(boolean c))) {
            throw new StatementException("condition of the if statement is not a boolean");
        }
        if (c) {
            state.getExeStack().push(thenS);
        } else {
            state.getExeStack().push(elseS);
        }
        return state;
    }

    @Override
    public String toString() {
        return "if " + cond + " then " + thenS + " else " + elseS;
    }
}
