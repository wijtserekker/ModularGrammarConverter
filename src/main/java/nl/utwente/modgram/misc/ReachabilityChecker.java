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

    /**
     * Determines which non-terminals can be reached from the given start non-terminal.
     *
     * It will throw a {@code NullPointerException} if the given start non-terminal does not exist.
     *
     * It requires a grammar of which the usage checker returned no errors and all the imports must be resolved.
     * @param grammar       The modular grammar containing the start non-terminal.
     * @param startModule   The module name of the module containing the start non-terminal.
     * @param startRule     The name of the start non-terminal.
     * @return  A set of non-terminals which can be reached from the start non-terminal (including the start non-terminal)
     * @see ReachabilityChecker#getReachableNonTermsFromRule(Rule, String, ModularGrammar)
     */
    public static HashSet<NonTermExpr> getReachableNonTerms(ModularGrammar grammar, String startModule, String startRule) {
        result = new HashSet<>();
        visitedRules = new LinkedList<>();
        Module module = grammar.getModule(startModule);
        Rule rule = module.getGrammarRule(startRule);
        getReachableNonTermsFromRule(rule, startModule, grammar);
        return result;
    }

    /**
     * A helper function of {@link ReachabilityChecker#getReachableNonTerms(ModularGrammar, String, String)}. It calls
     * {@link ReachabilityChecker#getReachableNonTermsFromRHS(ArrayList, String, ModularGrammar)} for every production
     * of the given rule. Also the non-terminal of the given rule is added to the result set.
     * @param rule          A reachable grammar rule.
     * @param moduleName    The module name of the reachable rule.
     * @param grammar       The whole modular grammar.
     * @see ReachabilityChecker#getReachableNonTerms(ModularGrammar, String, String)
     * @see ReachabilityChecker#getReachableNonTermsFromRHS(ArrayList, String, ModularGrammar)
     */
    private static void getReachableNonTermsFromRule(Rule rule, String moduleName, ModularGrammar grammar) {
        result.add(new NonTermExpr(moduleName, rule.getLeftHandSide()));
        if (visitedRules.contains(rule))
            return;
        visitedRules.add(rule);
        for (ArrayList<RHSElem> rhs : rule.getRightHandSides()) {
            getReachableNonTermsFromRHS(rhs, moduleName, grammar);
        }
    }

    /**
     * A helper function of {@link ReachabilityChecker#getReachableNonTermsFromRule(Rule, String, ModularGrammar)}. It calls
     * {@link ReachabilityChecker#getReachableNonTermsFromRule(Rule, String, ModularGrammar)} for every non-terminal it
     * encounters.
     * @param rightHandSide The production that this function should check for non-terminals.
     * @param moduleName    The current module name.
     * @param grammar       The whole modular grammar.
     * @see ReachabilityChecker#getReachableNonTermsFromRule(Rule, String, ModularGrammar)
     */
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
