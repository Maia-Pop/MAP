package interpreter.model.exp;

// enum (ADD, SUB, MUL, DIV) with a symbol for printing
public enum ArithOp {
    ADD("+"), SUB("-"), MUL("*"), DIV("/");

    private final String symbol;

    ArithOp(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
