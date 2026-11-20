package interpreter.model;

import interpreter.model.stmt.NopStmt;
import interpreter.model.type.IntType;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {
    @Test
    void ofCreatesAnEmptyHeap() {
        PrgState state = PrgState.of(new NopStmt());
        assertTrue(state.getHeap().getContent().isEmpty());
    }

    @Test
    void logStringEndsWithTheHeapSectionWithOneLinePerLocation() {
        PrgState state = PrgState.of(new NopStmt());
        state.getHeap().allocate(new IntValue(20));
        state.getHeap().allocate(new RefValue(1, new IntType()));

        assertTrue(state.toLogString().endsWith("Heap:\n1 --> 20\n2 --> (1, int)\n"));
    }
}
