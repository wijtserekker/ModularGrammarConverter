package nl.utwente.modgram.model.rhs;

public class CharsExpr extends RHSElem {

    private String text;

    public CharsExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
