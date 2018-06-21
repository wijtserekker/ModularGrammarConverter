package nl.utwente.modgram.model.rhs;

public class CharsExpr extends RHSElem {

    private String text;

    public CharsExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "[" + text + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CharsExpr) {
            return text.equals(((CharsExpr) o).text);
        }
        return false;
    }
}
