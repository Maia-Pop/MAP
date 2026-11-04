package interpreter.model.adt;

import interpreter.exceptions.ADTException;
// the ExeStack
// It holds: the statements still waiting to be executed
// ArrayDeque
public interface IStack<T> {
    void push(T v);

    T pop() throws ADTException;

    boolean isEmpty();
}
