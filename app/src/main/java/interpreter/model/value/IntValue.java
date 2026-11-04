package interpreter.model.value;

import interpreter.model.type.IType;
import interpreter.model.type.IntType;

// returns type
public record IntValue(int val) implements IValue {
    @Override
    public IType getType() {
        return new IntType();
    }
// returns number
    @Override
    public String toString() {
        return Integer.toString(val);
    }
}
