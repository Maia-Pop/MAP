package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag; // decides whether states are printed

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new StatementException("execution stack is empty");
        }
        // pops the top statement and calls its execute(state)
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    // runs the whole program:
    // - it gets the current state from the repository
    // - it displays the state, which is step 0 of the trace
    // - while the stack isn’t empty, it calls oneStep and displays the state again
    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        display(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            display(prg);
        }
    }

    // prints the state only when the flag is on(turned off for tests)
    private void display(PrgState prg) {
        if (displayFlag) {
            System.out.println(prg);
        }
    }
}
