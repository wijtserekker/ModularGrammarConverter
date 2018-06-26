package nl.utwente.modgram.misc;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import nl.utwente.modgram.model.Rule;
import nl.utwente.modgram.model.rhs.NonTermExpr;
import nl.utwente.modgram.model.rhs.ParExpr;
import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class ReachabilityChecker {

    private static HashSet<NonTermExpr> result;
    private static LinkedList<Rule> visitedRules;

    public static HashSet<NonTermExpr> getReachableNonTerms(ModularGrammar grammar, String startModule, String startRule) {
        result = new HashSet<>();
        visitedRules = new LinkedList<>();
        Module module = grammar.getModule(startModule);
        Rule rule = module.getGrammarRule(startRule);
        getReachableNonTermsFromRule(rule, startModule, grammar);
        return result;
    }

    private static void getReachableNonTermsFromRule(Rule rule, String moduleName, ModularGrammar grammar) {
        result.add(new NonTermExpr(moduleName, rule.getLeftHandSide()));
        if (visitedRules.contains(rule))
            return;
        visitedRules.add(rule);
        for (ArrayList<RHSElem> rhs : rule.getRightHandSides()) {
            getReachableNonTermsFromRHS(rhs, moduleName, grammar);
        }
    }

    private static void getReachableNonTermsFromRHS(ArrayList<RHSElem> rightHandSide, String moduleName, ModularGrammar grammar) {
        for (RHSElem elem : rightHandSide) {
            if (elem instanceof ParExpr) {
                getReachableNonTermsFromRHS(((ParExpr) elem).getElems(), moduleName, grammar);
            } else if (elem instanceof NonTermExpr) {
                NonTermExpr nonTerm = (NonTermExpr) elem;
                if (nonTerm.getModule() == null) {
                    getReachableNonTermsFromRule(grammar.getModule(moduleName).getGrammarRule(nonTerm.getName()), moduleName, grammar);
                } else {
                    getReachableNonTermsFromRule(grammar.getModule(nonTerm.getModule()).getGrammarRule(nonTerm.getName()), nonTerm.getModule(), grammar);
                }
            }
        }
    }

}
