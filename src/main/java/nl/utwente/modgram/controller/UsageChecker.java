package nl.utwente.modgram.controller;

import nl.utwente.modgram.model.*;
import nl.utwente.modgram.model.rhs.NonTermExpr;
import nl.utwente.modgram.model.rhs.ParExpr;
import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;

public class UsageChecker {

    private ArrayList<String> errors;
    private ModularGrammar grammar;

    /**
     * Checks the usa and declaration of names in the given modular grammar.
     * @param grammar   The modular grammar.
     * @return          A list of usage errors.
     */
    public ArrayList<String> checkUsageGrammar(ModularGrammar grammar) {
        this.errors = new ArrayList<>();
        this.grammar = grammar;

        for (Module module : grammar.getModules()) {
            checkUsageModule(module);
        }
        return errors;
    }

    private void checkUsageModule(Module module) {
        if (module.getDependencies().contains(module.getName())) {
            errors.add("Can not use module '" + module.getName() + "' in module '" + module.getName() + "'!");
        }
        for (String dependency : module.getDependencies()) {
            if (grammar.getModule(dependency) == null)
                errors.add("Dependency module '" + dependency + "' in module '" + module.getName() + "' does not exist!");
        }
        for (ImportRule rule : module.getImportRules()) {
            checkUsageImportRule(rule, module);
        }
        for (Rule rule : module.getGrammarRules()) {
            checkUsageGrammarRule(rule, module);
        }
    }

    private void checkUsageImportRule(ImportRule rule, Module module) {
        if (!module.getDependencies().contains(rule.getImportModule())) {
            errors.add("Usage of module '" + rule.getImportModule() + "' in rule '" + rule.toString() + "' is not defined in module '" + module.getName() + "'!");
        } else if (!grammar.getModule(rule.getImportModule()).containsRule(rule.getImportRule())) {
            errors.add("Imported rule '" + rule.getImportModule() + "." + rule.getImportRule() + "' in module '" + module.getName() + "' does not exist!");
        }
    }

    private void checkUsageGrammarRule(Rule rule, Module module) {
        for (ArrayList<RHSElem> righHandSide : rule.getRightHandSides())
            for (RHSElem rhsElem : righHandSide)
                checkUsageRHSElem(rhsElem, rule, module);
    }

    private void checkUsageRHSElem(RHSElem rhsElem, Rule rule, Module module) {
        if (rhsElem instanceof NonTermExpr) {
            checkUsageNonTermExr((NonTermExpr) rhsElem, rule, module);
        } else if (rhsElem instanceof ParExpr) {
            for (RHSElem elem : ((ParExpr) rhsElem).getElems())
                checkUsageRHSElem(elem, rule, module);
        }
    }

    private void checkUsageNonTermExr(NonTermExpr rhsElem, Rule rule, Module module) {
        if (rhsElem.getModule() != null) {
            if (!module.getDependencies().contains(rhsElem.getModule())) {
                errors.add("Usage of module '" + rhsElem.getModule() + "' in rule '" + rule.getLeftHandSide() + "' is not defined in module '" + module.getName() + "'!");
            } else if (!grammar.getModule(rhsElem.getModule()).containsRule(rhsElem.getName())) {
                errors.add("Non-terminal '" + rhsElem.getModule() + "." + rhsElem.getName() + "' in module '" + module.getName() + "' does not exist!");
            }
        } else {
            if (module.getGrammarRule(rhsElem.getName()) == null)
                errors.add("Non-terminal '" + rhsElem.getName() + "' in rule '" + rule.getLeftHandSide() +"' is not defined in module '" + module.getName() + "'!");
        }
    }


}
