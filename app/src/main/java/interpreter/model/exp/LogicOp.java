package interpreter.model.exp;

public enum LogicOp {
    AND("and"), OR("or");

    private final String symbol;

    // both sides are always evaluated
    LogicOp(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
