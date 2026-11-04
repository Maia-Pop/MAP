package interpreter.model.adt;

import interpreter.exceptions.ADTException;
// SymTable
// It holds: the declared variables, mapping each variable name to its current value
// HashMap
public interface IDictionary<K, V> {
    boolean isDefined(K key);

    // returns variable with certain key, also throws on a missing key
    V lookup(K key) throws ADTException;

    // adds a new variable (used when declaring)
    void put(K key, V value);

    // changes an existing one and throws if the key is missing
    void update(K key, V value) throws ADTException;
}
