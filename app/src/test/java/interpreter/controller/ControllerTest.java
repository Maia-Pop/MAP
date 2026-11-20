package interpreter.controller;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.stmt.AssignStmt;
import interpreter.model.stmt.CompStmt;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NewStmt;
import interpreter.model.stmt.VarDeclStmt;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
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
    void logFileContainsTheFiveSectionsInOrder() throws Exception {
        run(Examples.EX1);
        String log = Files.readString(dir.resolve("log.txt"));
        int a = log.indexOf("ExeStack:"), b = log.indexOf("SymTable:"),
                c = log.indexOf("Out:"), d = log.indexOf("FileTable:"),
                e = log.indexOf("Heap:");
        assertTrue(0 <= a && a < b && b < c && c < d && d < e);
    }

    @Test
    void logFileHoldsTheInitialStateAndTwoStatesForEveryStep() throws Exception {
        run(Examples.EX1);   // 5 steps: before and after the garbage collection
        String log = Files.readString(dir.resolve("log.txt"));
        assertEquals(11, log.split("ExeStack:", -1).length - 1);
    }

    @Test
    void theGarbageCollectorRemovesTheLocationsThatNobodyReaches() throws MyException {
        // Ref int v; new(v, 20); new(v, 30): location 1 is lost when v points to location 2
        PrgState state = run(new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new NewStmt("v", new ValueExp(new IntValue(30))))));
        assertEquals(1, state.getHeap().getContent().size());
        assertFalse(state.getHeap().isDefined(1));
        assertEquals(new IntValue(30), state.getHeap().get(2));
    }

    @Test
    void fileExamplePrintsBothNumbersAndEndsWithAnEmptyFileTable() throws Exception {
        Path in = dir.resolve("test.in");
        Files.writeString(in, "15\n50\n");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(2, state.getOut().size());
        assertEquals(new IntValue(15), state.getOut().get(0));
        assertEquals(new IntValue(50), state.getOut().get(1));
        assertTrue(state.getFileTable().toMap().isEmpty());
    }

    @Test
    void fileExampleWithAnEmptyFilePrintsTwoZeros() throws Exception {
        Path in = dir.resolve("empty.in");
        Files.writeString(in, "");
        PrgState state = run(Examples.fileExample(in.toString()));
        assertEquals(new IntValue(0), state.getOut().get(0));
        assertEquals(new IntValue(0), state.getOut().get(1));
    }

    @Test
    void fileExampleWithAMissingFileStopsWithAnError() {
        String missing = dir.resolve("missing.in").toString();
        assertThrows(StatementException.class, () -> run(Examples.fileExample(missing)));
    }

    @Test
    void example5PrintsFive() throws MyException {
        PrgState state = run(Examples.EX5);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }
}
