package nl.utwente.modgram.model.rhs;

public class Operator extends RHSElem {
    public enum Type {
        WILDCARD,
        ONE_OR_MORE,
        ZERO_OR_MORE,
        ZERO_OR_ONE,
        NOT,
        OR
    }

    private Type type;

    public Operator(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
