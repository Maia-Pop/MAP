package interpreter.model.value;

import interpreter.model.type.IType;

// says whether a value is an int or a bool
public interface IValue {
    IType getType();
}
