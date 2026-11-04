package interpreter.model.type;

import interpreter.model.value.IValue;

// int - 0        bool - false
public interface IType {
    IValue defaultValue();
}
