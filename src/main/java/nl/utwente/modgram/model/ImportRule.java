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
    private boolean induced;

    /**
     * The constructor of an import rule model.
     * @param type          The type of import rule
     * @param localRule     The name of the non-terminal this rule imports to
     * @param importModule  The name of the module this rule imports from
     * @param importRule    The name of the rule this module imports from
     * @param induced       {@code true} if the rule was created from a import-recursive rule, else {@code false}
     */
    public ImportRule(Type type, String localRule, String importModule, String importRule, boolean induced) {
        this.type = type;
        this.localRule = localRule;
        this.importModule = importModule;
        this.importRule = importRule;
        this.induced = induced;
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

    public boolean isInduced() {
        return induced;
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
