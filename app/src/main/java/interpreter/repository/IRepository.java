package interpreter.repository;

import interpreter.model.PrgState;

// returns the current program state
public interface IRepository {
    PrgState getCrtPrg();
}
