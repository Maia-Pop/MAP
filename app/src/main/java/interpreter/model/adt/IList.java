package interpreter.model.adt;

import interpreter.exceptions.ADTException;

public interface IList<T> {
    void add(T element);

    T get(int index) throws ADTException;

    int size();
}
