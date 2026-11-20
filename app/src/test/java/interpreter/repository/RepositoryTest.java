package interpreter.repository;

import interpreter.TestSupport;
import interpreter.exceptions.MyException;
import interpreter.exceptions.RepositoryException;
import interpreter.model.PrgState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @TempDir
    Path dir; // gives each test its own empty file then deletes it

    @Test
    void getCrtPrgReturnsTheStoredState() { // creates a program state and a repository holding it
        PrgState state = TestSupport.emptyState();
        IRepository repo = new Repository(state, dir.resolve("log.txt").toString());
        assertSame(state, repo.getCrtPrg());
    }

    @Test
    void logPrgStateExecAppendsTheStateToTheLogFile() throws Exception {
        Path log = dir.resolve("log.txt");
        IRepository repo = new Repository(TestSupport.emptyState(), log.toString());
        repo.logPrgStateExec();
        repo.logPrgStateExec();

        String text = Files.readString(log);
        assertEquals(2, text.split("ExeStack:", -1).length - 1);
    }

    @Test
    void logToAnUnwritablePathThrows() {
        String badPath = dir.resolve("no/such/folder/log.txt").toString();
        IRepository repo = new Repository(TestSupport.emptyState(), badPath);
        assertThrows(RepositoryException.class, repo::logPrgStateExec);
    }
}
