package nl.utwente.modgram.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Module {

    private String name;
    private HashSet<String> dependencies = new HashSet<>();
    private ArrayList<ImportRule> importRules = new ArrayList<>();
    private HashMap<String, Rule> grammarRules = new HashMap<>();
    private ArrayList<RemoveRule> removeRules = new ArrayList<>();

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void addRemoveRule(RemoveRule removeRule) {
        removeRules.add(removeRule);
    }

    public Rule getGrammarRule(String leftHandSide) {
        return grammarRules.get(leftHandSide);
    }

    public HashSet<String> getDependencies() {
        return dependencies;
    }

    public ArrayList<ImportRule> getImportRules() {
        return importRules;
    }

    public Iterable<? extends Rule> getGrammarRules() {
        return grammarRules.values();
    }

    public ArrayList<RemoveRule> getRemoveRules() {
        return removeRules;
    }

    public boolean containsRule(String lhs) {
        for (ImportRule rule : importRules)
            if (rule.getLocalRule().equals(lhs))
                return true;
        return grammarRules.containsKey(lhs);
    }

    public boolean containsImportRuleWithLHS(String lhs) {
        for (ImportRule rule : importRules)
            if (rule.getLocalRule().equals(lhs))
                return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("module " + name + ";\n");
        for (String dependency : dependencies) {
            result.append("using ").append(dependency).append('\n');
        }
        result.append('\n');
        for (ImportRule importRule : importRules) {
            result.append(importRule.toString()).append('\n');
        }
        for (Rule rule : grammarRules.values()) {
            result.append(rule.toString()).append('\n');
        }
        for (RemoveRule removeRule : removeRules) {
            result.append(removeRule.toString()).append('\n');
        }
        return result.toString();
    }
}
