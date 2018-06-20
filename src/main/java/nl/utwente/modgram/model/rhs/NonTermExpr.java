package nl.utwente.modgram.model.rhs;

public class NonTermExpr extends RHSElem {

    private String module;
    private String name;

    public NonTermExpr(String module, String name) {
        this.module = module;
        this.name = name;
    }

    public NonTermExpr(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return (module == null ? "" : module + ".") + name;
    }
}
