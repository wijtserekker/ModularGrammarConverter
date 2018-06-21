package nl.utwente.modgram.model;

public class ImportRule {

    public enum Type {
        REFERENCE,
        CLONE,
        CLONE_RECURSIVE;

        @Override
        public String toString() {
            switch (this) {
                case REFERENCE:
                    return "<-";
                case CLONE:
                    return "<=";
                case CLONE_RECURSIVE:
                    return "<=*";
                default:
                    return null; //Will never happen
            }
        }
    }

    private Type type;
    private String localRule;
    private String importModule;
    private String importRule;

    public ImportRule(Type type, String localRule, String importModule, String importRule) {
        this.type = type;
        this.localRule = localRule;
        this.importModule = importModule;
        this.importRule = importRule;
    }

    public Type getType() {
        return type;
    }

    public String getLocalRule() {
        return localRule;
    }

    public String getImportModule() {
        return importModule;
    }

    public String getImportRule() {
        return importRule;
    }

    @Override
    public String toString() {
        return localRule + " " + type.toString() + " " + importModule + "." + importRule;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ImportRule) {
            ImportRule rule = (ImportRule) o;
            return type == rule.type && localRule.equals(rule.localRule) && importRule.equals(rule.importRule)
                    && (importModule == null ? rule.importModule == null : importModule.equals(rule.importModule));
        }
        return false;
    }
}
