package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IntValue;
import interpreter.model.value.IValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
//heap
class MyHeapTest {
    private IHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap();
    }

    @Test
    void allocateReturnsTheAddressesOneTwoThree() {
        assertEquals(1, heap.allocate(new IntValue(10)));
        assertEquals(2, heap.allocate(new IntValue(20)));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void getReturnsTheStoredValue() throws ADTException {
        int address = heap.allocate(new IntValue(10));
        assertEquals(new IntValue(10), heap.get(address));
    }

    @Test
    void getOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.get(1));
    }

    @Test
    void updateChangesTheValue() throws ADTException {
        int address = heap.allocate(new IntValue(10));
        heap.update(address, new IntValue(99));
        assertEquals(new IntValue(99), heap.get(address));
    }

    @Test
    void updateOfAMissingAddressThrows() {
        assertThrows(ADTException.class, () -> heap.update(1, new IntValue(5)));
    }

    @Test
    void isDefinedIsTrueOnlyForAllocatedAddresses() {
        int address = heap.allocate(new IntValue(10));
        assertTrue(heap.isDefined(address));
        assertFalse(heap.isDefined(address + 1));
    }

    @Test
    void getContentIsAReadOnlyCopy() {
        heap.allocate(new IntValue(10));
        assertEquals(1, heap.getContent().size());
        assertThrows(UnsupportedOperationException.class,
                () -> heap.getContent().put(5, new IntValue(1)));
    }

    @Test
    void setContentReplacesTheContentButAddressesAreNotReused() {
        heap.allocate(new IntValue(10));
        heap.allocate(new IntValue(20));
        heap.setContent(Map.<Integer, IValue>of(2, new IntValue(20)));

        assertFalse(heap.isDefined(1));
        assertTrue(heap.isDefined(2));
        assertEquals(3, heap.allocate(new IntValue(30)));
    }

    @Test
    void toStringShowsAddressArrowValueInOrder() {
        heap.allocate(new IntValue(20));
        heap.allocate(new IntValue(30));
        assertEquals("{1 -> 20, 2 -> 30}", heap.toString());
    }
}
