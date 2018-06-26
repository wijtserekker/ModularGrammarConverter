package nl.utwente.modgram.controller;

import nl.utwente.modgram.misc.RuleUtils;
import nl.utwente.modgram.model.*;
import nl.utwente.modgram.model.rhs.NonTermExpr;
import nl.utwente.modgram.model.rhs.ParExpr;
import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;
import java.util.HashSet;

public class ImportResolver {

    public static void resolveImports(ModularGrammar grammar, ArrayList<String> order) {
        for (String moduleName : order) {
            Module module = grammar.getModule(moduleName);
            resolveImportByCloneRecursive(module, grammar);
            resolveOtherImportRules(module, grammar);
            resolveRemoveRules(module, grammar);
        }
    }

    private static void resolveImportByCloneRecursive(Module module, ModularGrammar grammar) {
        // Get all CLONE_RECURSIVE import rules from the module
        ArrayList<ImportRule> importRules = new ArrayList<>();
        for (ImportRule importRule : module.getImportRules()) {
            if (importRule.getType() == ImportRule.Type.CLONE_RECURSIVE)
                importRules.add(importRule);
        }

        // Remove all the CLONE_RECURSIVE import rules from the module
        module.getImportRules().removeAll(importRules);

        // Resolve every CLONE_RECURSIVE rule by generating CLONE rules which ultimately achieve the same thing
        for (ImportRule importRule : importRules) {
            module.addImportRule(new ImportRule(ImportRule.Type.CLONE, importRule.getLocalRule()
                    , importRule.getImportModule(), importRule.getImportRule(), true));
            ArrayList<ImportRule> inducedRules =
                    getInducedCloneRules(importRule.getImportModule(), importRule.getImportRule(), grammar);

            // Only add a CLONE rule if it does not already exist
            for (ImportRule rule : inducedRules)
                if (!module.getImportRules().contains(rule))
                    module.addImportRule(rule);

        }
    }

    private static ArrayList<ImportRule> getInducedCloneRules(String importModule, String importRule, ModularGrammar grammar) {
        // Get all the non-terminals from the rhs of the imported rule
        Module module = grammar.getModule(importModule);
        Rule rule = module.getGrammarRule(importRule);
        HashSet<NonTermExpr> nonTermExprs = new HashSet<>();
        for (ArrayList<RHSElem> rhs : rule.getRightHandSides()) {
            getNonTermExprs(rhs, nonTermExprs);
        }

        // Remove non-terminal which is the same as the base non-terminal of the imported rule
        nonTermExprs.remove(new NonTermExpr(null, importRule));

        // Generate import rules
        ArrayList<ImportRule> result = new ArrayList<>();
        for (NonTermExpr nonTerm : nonTermExprs) {
            if (nonTerm.getModule() == null) {
                result.add(new ImportRule(ImportRule.Type.CLONE, nonTerm.getName(), importModule, nonTerm.getName(), true));
            } else {
                result.add(new ImportRule(ImportRule.Type.CLONE, nonTerm.getName(), nonTerm.getModule(), nonTerm.getName(), true));
                result.addAll(getInducedCloneRules(nonTerm.getModule(), nonTerm.getName(), grammar));
            }
        }

        return result;
    }

    private static void getNonTermExprs(ArrayList<RHSElem> rhs, HashSet<NonTermExpr> nonTermExprs) {
        for (RHSElem rhsElem : rhs) {
            if (rhsElem instanceof NonTermExpr) {
                nonTermExprs.add((NonTermExpr) rhsElem);
            } else if (rhsElem instanceof ParExpr) {
                getNonTermExprs(((ParExpr) rhsElem).getElems(), nonTermExprs);
            }
        }
    }

    private static void resolveOtherImportRules(Module module, ModularGrammar grammar) {
        for (ImportRule rule : module.getImportRules()) {
            switch (rule.getType()) {
                case REFERENCE:
                    resolveReferenceRule(rule, module, grammar);
                    break;
                case CLONE:
                    resolveCloneRule(rule, module, grammar);
                    break;
            }
        }
        module.getImportRules().clear();
    }

    private static void resolveReferenceRule(ImportRule rule, Module module, ModularGrammar grammar) {
        Rule importedRule = grammar.getModule(rule.getImportModule()).getGrammarRule(rule.getImportRule());
        // Make a copy of the rightHandSides of the imported rule
        ArrayList<ArrayList<RHSElem>> rightHandSides = RuleUtils.copyRightHandSides(importedRule.getRightHandSides());
        // Change all the non-terminals X to M.X in the rightHandSides. Where M is the module the rule is imported from
        RuleUtils.changeNormalNonTermsToImportedNonTermsRHSs(rightHandSides, rule.getImportModule());
        // If a grammar rule with this non-terminal does not already exist
        if (module.getGrammarRule(rule.getLocalRule()) == null) {
            // Create a new grammar rule with the induced rightHandSides
            module.addGrammarRule(new Rule(rule.getLocalRule(), rightHandSides));
        } else {
            // Add the rightHandSides to the existing rule
            RuleUtils.addRHSsNoDuplicateProductions(module.getGrammarRule(rule.getLocalRule()), rightHandSides);
        }
    }

    private static void resolveCloneRule(ImportRule rule, Module module, ModularGrammar grammar) {
        Rule importedRule = grammar.getModule(rule.getImportModule()).getGrammarRule(rule.getImportRule());
        // Make a copy of the rightHandSides of the imported rule
        ArrayList<ArrayList<RHSElem>> rightHandSides = RuleUtils.copyRightHandSides(importedRule.getRightHandSides());
        // If the CLONE rule was induced by a CLONE_RECURSIVE rule
        if (rule.isInduced()) {
            // Replace all non terminals Y with X in the right hand sides where Y and X in X <= M.Y
            RuleUtils.replaceNontermRHSs(rule.getImportRule(), rule.getLocalRule(), rightHandSides);
            // Change all the non-terminals M.X to X in the rightHandSides
            RuleUtils.changeAllImportedNonTermsToNormalNonTermsRHSs(rightHandSides);
        } else {
            // Change all the non-terminals X to M.X in the rightHandSides. Where M is the module the rule is imported from
            RuleUtils.changeNormalNonTermsToImportedNonTermsRHSs(rightHandSides, rule.getImportModule());
            // Change only the non-terminals M.X to X in the rightHandSides where M and X are from the import rule X <= M.X
            RuleUtils.changeSelectImportedNonTermsToNormalNonTermsRHSs(rightHandSides, rule.getImportModule(), rule.getImportRule());
            // Replace all non terminals Y with X in the right hand sides where Y and X in X <= M.Y
            RuleUtils.replaceNontermRHSs(rule.getImportRule(), rule.getLocalRule(), rightHandSides);
        }
        // If a grammar rule with this non-terminal does not already exist
        if (module.getGrammarRule(rule.getLocalRule()) == null) {
            // Create a new grammar rule with the induced rightHandSides
            module.addGrammarRule(new Rule(rule.getLocalRule(), rightHandSides));
        } else {
            // Add the rightHandSides to the existing rule
            RuleUtils.addRHSsNoDuplicateProductions(module.getGrammarRule(rule.getLocalRule()), rightHandSides);
        }
    }

    private static void resolveRemoveRules(Module module, ModularGrammar grammar) {
        for (RemoveRule removeRule : module.getRemoveRules()) {
            Rule rule = module.getGrammarRule(removeRule.getLeftHandSide());
            boolean removed = rule.getRightHandSides().remove(removeRule.getRighHandSide());
            if (!removed)
                System.out.println("Warning: rule '" + removeRule.toString() + "' did not remove any productions.");
        }
        module.getRemoveRules().clear();
    }

}
