package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

// returns the state
public interface IStmt {
    PrgState execute(PrgState state) throws MyException;
}
