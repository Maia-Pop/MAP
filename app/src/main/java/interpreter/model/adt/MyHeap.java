package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import interpreter.model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyHeap implements IHeap {
    private Map<Integer, IValue> heap = new HashMap<>();
    private int nextFree = 1;   // address 0 means null

    @Override
    public int allocate(IValue value) {
        int address = nextFree++;
        heap.put(address, value);
        return address;
    }

    @Override
    public IValue get(int address) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("address " + address + " is not in the heap");
        }
        return heap.get(address);
    }

    @Override
    public void update(int address, IValue value) throws ADTException {
        if (!heap.containsKey(address)) {
            throw new ADTException("address " + address + " is not in the heap");
        }
        heap.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return Map.copyOf(heap);
    }

    @Override
    public void setContent(Map<Integer, IValue> content) {
        heap = new HashMap<>(content);
    }

    // {1 -> 20, 2 -> (1, int)}, the addresses in order
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Integer, IValue> entry : new TreeMap<>(heap).entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue());
            first = false;
        }
        return sb.append("}").toString();
    }
}
