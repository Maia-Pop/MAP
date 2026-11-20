package interpreter.controller;

import interpreter.model.value.IValue;
import interpreter.model.value.RefValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class GarbageCollector {
    private GarbageCollector() {
    }

    /** The addresses that occur in the RefValues of the collection. */
    public static Set<Integer> getAddrFromSymTable(Collection<IValue> values) {
        return values.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).address())
                .collect(Collectors.toSet());
    }

    /** The addresses reachable from the roots, also through references stored in the heap. */
    public static Set<Integer> reachableAddresses(Set<Integer> roots, Map<Integer, IValue> heap) {
        Set<Integer> reachable = new HashSet<>(roots);
        Set<Integer> frontier = new HashSet<>(roots);   // found, but not examined yet
        while (!frontier.isEmpty()) {
            List<IValue> contents = frontier.stream()
                    .filter(heap::containsKey)
                    .map(heap::get)
                    .toList();
            Set<Integer> found = new HashSet<>(getAddrFromSymTable(contents));
            found.removeAll(reachable);
            reachable.addAll(found);
            frontier = found;
        }
        return reachable;
    }

    /** SAFE: keeps the heap entries reachable from the SymTable, directly or through the heap. */
    public static Map<Integer, IValue> safeGarbageCollector(Set<Integer> symTableAddr,
                                                            Map<Integer, IValue> heap) {
        Set<Integer> reachable = reachableAddresses(symTableAddr, heap);
        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
