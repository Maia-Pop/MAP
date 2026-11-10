package interpreter.model.stmt;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.StringType;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStmtTest {
    @TempDir
    Path dir;   // a new empty folder for every test

    private PrgState state;
    private StringValue fileName;
    private ValueExp file;   // the expression "<dir>/numbers.in", a file with the lines 7 and 8

    @BeforeEach
    void setUp() throws Exception {
        Path f = dir.resolve("numbers.in");
        Files.writeString(f, "7\n8\n");
        fileName = new StringValue(f.toString());
        file = new ValueExp(fileName);
        state = TestSupport.emptyState();
        new VarDeclStmt("x", new IntType()).execute(state);
    }

    // ---------- openRFile (S8) ----------

    @Test
    void openAddsTheFileToTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));
    }

    @Test
    void openWithANonStringExpressionThrows() {
        var stmt = new OpenRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void openingTheSameFileTwiceThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(file).execute(state));
    }

    @Test
    void openingAMissingFileThrows() {
        var missing = new ValueExp(new StringValue(dir.resolve("missing.in").toString()));
        assertThrows(StatementException.class,
                () -> new OpenRFileStmt(missing).execute(state));
    }

    // ---------- readFile (S9) ----------

    @Test
    void readGivesTheNumbersInOrderThenZeroAtTheEnd() throws MyException {
        new OpenRFileStmt(file).execute(state);
        var read = new ReadFileStmt(file, "x");

        read.execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("x"));
        read.execute(state);
        assertEquals(new IntValue(8), state.getSymTable().lookup("x"));
        read.execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("x"));
    }

    @Test
    void readIntoAnUndeclaredVariableThrows() throws MyException {
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "y").execute(state));
    }

    @Test
    void readIntoAVariableThatIsNotAnIntThrows() throws MyException {
        new VarDeclStmt("s", new StringType()).execute(state);
        new OpenRFileStmt(file).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "s").execute(state));
    }

    @Test
    void readWithANonStringExpressionThrows() {
        var stmt = new ReadFileStmt(new ValueExp(new IntValue(1)), "x");
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void readFromAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(file, "x").execute(state));
    }

    @Test
    void readALineThatIsNotAnIntegerThrows() throws Exception {
        Path bad = dir.resolve("bad.in");
        Files.writeString(bad, "abc\n");
        var badFile = new ValueExp(new StringValue(bad.toString()));
        new OpenRFileStmt(badFile).execute(state);
        assertThrows(StatementException.class,
                () -> new ReadFileStmt(badFile, "x").execute(state));
    }

    // ---------- closeRFile (S10) ----------

    @Test
    void closeRemovesTheFileFromTheFileTable() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        assertFalse(state.getFileTable().isDefined(fileName));
    }

    @Test
    void closeWithANonStringExpressionThrows() {
        var stmt = new CloseRFileStmt(new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> stmt.execute(state));
    }

    @Test
    void closingAFileThatIsNotOpenThrows() {
        assertThrows(StatementException.class,
                () -> new CloseRFileStmt(file).execute(state));
    }

    @Test
    void aClosedFileCanBeOpenedAgain() throws MyException {
        new OpenRFileStmt(file).execute(state);
        new CloseRFileStmt(file).execute(state);
        new OpenRFileStmt(file).execute(state);
        assertTrue(state.getFileTable().isDefined(fileName));
    }
}
