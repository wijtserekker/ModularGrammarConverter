package nl.utwente.modgram.model.rhs;

import java.util.Objects;

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

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return (module == null ? "" : module + ".") + name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NonTermExpr) {
            NonTermExpr nonTermExpr = (NonTermExpr) o;
            return name.equals(nonTermExpr.name)
                    && (module == null ? nonTermExpr.module == null : module.equals(nonTermExpr.module));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, name);
    }
}
