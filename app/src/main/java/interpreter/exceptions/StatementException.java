package interpreter.exceptions;

// an error while executing a statement

public class StatementException extends MyException {
    public StatementException(String message) {
        super(message);
    }
}
