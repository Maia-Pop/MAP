package interpreter.exceptions;

// an error while evaluating an expression

public class ExpressionException extends MyException {
    public ExpressionException(String message) {
        super(message);
    }
}
