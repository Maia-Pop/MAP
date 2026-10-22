package interpreter.repository;

import interpreter.TestSupport;
import interpreter.model.PrgState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @Test
    void getCrtPrgReturnsTheStoredState() {
        PrgState state = TestSupport.emptyState();
        IRepository repo = new Repository(state);
        assertSame(state, repo.getCrtPrg());
    }
}
