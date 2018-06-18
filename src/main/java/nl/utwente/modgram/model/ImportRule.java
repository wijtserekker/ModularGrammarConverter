package nl.utwente.modgram.model;

public class ImportRule {

    public enum Type {
        CLONE,
        REFERENCE,
        REFERENCE_RECURSIVE
    }

    private Type type;
    private Module module;
    private String localRule;
    private String importModule;
    private String importRule;

    public ImportRule(Type type, Module module, String localRule, String importModule, String importRule) {
        this.type = type;
        this.module = module;
        this.localRule = localRule;
        this.importModule = importModule;
        this.importRule = importRule;
    }

    public Type getType() {
        return type;
    }

    public Module getModule() {
        return module;
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
}
