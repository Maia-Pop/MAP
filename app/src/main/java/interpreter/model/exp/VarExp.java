package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;

// looks the variable up in the SymTable. if it isn’t defined, it throws “variable x is not defined”
public record VarExp(String id) implements IExp {   // E2
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        if (!symTable.isDefined(id)) {
            throw new ExpressionException("variable " + id + " is not defined");
        }
        return symTable.lookup(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
