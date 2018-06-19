package nl.utwente.modgram.model.rhs;

public class NonTermExpr extends RHSElem {

    private String module;
    private String name;
    private boolean isToken;

    public NonTermExpr(String module, String name, boolean isToken) {
        this.module = module;
        this.name = name;
        this.isToken = isToken;
    }

    public NonTermExpr(String name, boolean isToken) {
        this.name = name;
        this.isToken = isToken;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public boolean isToken() {
        return isToken;
    }
}
