package nl.utwente.modgram.model.rhs;

public class StrExpr extends RHSElem {

    private String text;

    public StrExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "'" + text + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StrExpr) {
            return text.equals(((StrExpr) o).text);
        }
        return false;
    }
}
