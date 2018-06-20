package nl.utwente.modgram.model.rhs;

public class Operator extends RHSElem {
    public enum Type {
        WILDCARD,
        ONE_OR_MORE,
        ZERO_OR_MORE,
        ZERO_OR_ONE,
        NOT,
        OR;

        @Override
        public String toString() {
            switch (this) {
                case WILDCARD:
                    return "$";
                case ONE_OR_MORE:
                    return "+";
                case ZERO_OR_MORE:
                    return "*";
                case ZERO_OR_ONE:
                    return "?";
                case NOT:
                    return "~";
                case OR:
                    return "|";
                default:
                    return null; //Will never happen
            }
        }
    }

    private Type type;

    public Operator(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
