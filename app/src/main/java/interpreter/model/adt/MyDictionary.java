package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K, V> implements IDictionary<K, V> {
    private final Map<K, V> map = new HashMap<>();

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public V lookup(K key) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("key " + key + " is not in the dictionary");
        }
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) throws ADTException {
        if (!map.containsKey(key)) {
            throw new ADTException("key " + key + " is not in the dictionary");
        }
        map.put(key, value);
    }

    // {a -> 2, b -> 3}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue());
            first = false;
        }
        return sb.append("}").toString();
    }
}
