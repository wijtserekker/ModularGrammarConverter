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
}
