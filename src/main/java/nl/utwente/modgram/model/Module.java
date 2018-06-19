package nl.utwente.modgram.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Module {

    private String name;
    private HashSet<String> dependencies = new HashSet<>();
    private ArrayList<ImportRule> importRules = new ArrayList<>();
    private HashMap<String, Rule> grammarRules = new HashMap<>();

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return null;
    }

    public void addDependency(String moduleName) {
        dependencies.add(moduleName);
    }

    public void addImportRule(ImportRule rule) {
        importRules.add(rule);
    }

    public void addGrammarRule(Rule rule) {
        grammarRules.put(rule.getLeftHandSide(), rule);
    }


}
