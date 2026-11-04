package interpreter.model.adt;

import interpreter.exceptions.ADTException;
// Out
// It holds: every value printed so far, in order
// ArrayList
public interface IList<T> {
    void add(T element);

    T get(int index) throws ADTException;

    int size();
}
