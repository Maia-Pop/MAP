package interpreter.model.adt;

import interpreter.exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    private final List<T> list = new ArrayList<>();

    @Override
    public void add(T element) {
        list.add(element);
    }

    @Override
    public T get(int index) throws ADTException {
        if (index < 0 || index >= list.size()) {
            throw new ADTException("index " + index + " is out of bounds");
        }
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    // {1, 2, 3}
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(list.get(i));
        }
        return sb.append("}").toString();
    }
}
