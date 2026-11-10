package interpreter.controller;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.value.IntValue;
import interpreter.repository.Repository;
import interpreter.view.Examples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @TempDir
    Path dir;

    private Controller controllerFor(PrgState state) {
        var repo = new Repository(state, dir.resolve("log.txt").toString());
        return new Controller(repo, false);   // display off
    }

    private PrgState run(IStmt program) throws MyException {
        PrgState state = PrgState.of(program);
        controllerFor(state).allStep();
        return state;
    }

    @Test
    void example1PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX1);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void example2PrintsEighteen() throws MyException {
        PrgState state = run(Examples.EX2);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(18), state.getOut().get(0));
        assertEquals(new IntValue(17), state.getSymTable().lookup("a"));
        assertEquals(new IntValue(18), state.getSymTable().lookup("b"));
    }

    @Test
    void example3PrintsTwo() throws MyException {
        PrgState state = run(Examples.EX3);
        assertTrue(state.getExeStack().isEmpty());
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(2), state.getOut().get(0));
    }

    @Test
    void example1FollowsTheTraceFromTheLabDocument() throws MyException {
        PrgState state = PrgState.of(Examples.EX1);
        Controller controller = controllerFor(state);

        controller.oneStep(state);   // S1: split int v; (...)
        assertEquals("{}", state.getSymTable().toString());

        controller.oneStep(state);   // S3: int v
        assertEquals("{v -> 0}", state.getSymTable().toString());

        controller.oneStep(state);   // S1: split v = 2; print(v)
        controller.oneStep(state);   // S2: v = 2
        assertEquals("{v -> 2}", state.getSymTable().toString());
        assertEquals("{}", state.getOut().toString());

        controller.oneStep(state);   // S4: print(v)
        assertEquals("{2}", state.getOut().toString());
        assertTrue(state.getExeStack().isEmpty());
    }

    @Test
    void oneStepOnEmptyStackThrows() {
        PrgState state = TestSupport.emptyState();
        Controller controller = controllerFor(state);
        var ex = assertThrows(StatementException.class, () -> controller.oneStep(state));
        assertEquals("execution stack is empty", ex.getMessage());
    }

    @Test
    void errorInTheProgramStopsTheExecution() {
        IStmt bad = new AssignStmt("x", new ValueExp(new IntValue(1)));   // x never declared
        assertThrows(StatementException.class, () -> run(bad));
    }

    @Test
    void logFileContainsTheFourSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:");
        assertTrue(0 <= a && a < b && b < c && c < d);
    }

    @Test
    void logFileHoldsTheInitialStateAndTheStateAfterEveryStep() throws Exception {
        run(Examples.EX1);   // 5 steps
        String log = Files.readString(dir.resolve("log.txt"));
        assertEquals(6, log.split("ExeStack:", -1).length - 1);
    }
}
