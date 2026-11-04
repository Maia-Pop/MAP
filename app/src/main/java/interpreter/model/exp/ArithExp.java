package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

// evaluates the left side, then the right
public record ArithExp(ArithOp op, IExp left, IExp right) implements IExp {   // E3
    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        // checks that the result is an int and extracts the number in one step. If it isn’t, it throws “first/second operand is not an integer”
        if (!(left.eval(symTable) instanceof IntValue(int n1))) {
            throw new ExpressionException("first operand is not an integer");
        }
        if (!(right.eval(symTable) instanceof IntValue(int n2))) {
            throw new ExpressionException("second operand is not an integer");
        }
        // computes the result
        return switch (op) {
            case ADD -> new IntValue(n1 + n2);
            case SUB -> new IntValue(n1 - n2);
            case MUL -> new IntValue(n1 * n2);
            case DIV -> {
                // checks for zero first
                if (n2 == 0) {
                    throw new ExpressionException("division by zero");
                }
                yield new IntValue(n1 / n2);
            }
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}
