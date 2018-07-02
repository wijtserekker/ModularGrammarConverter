package nl.utwente.modgram.misc;

import nl.utwente.modgram.model.Rule;
import nl.utwente.modgram.model.rhs.NonTermExpr;
import nl.utwente.modgram.model.rhs.ParExpr;
import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;

public class RuleUtils {

    /**
     * Creates a copy of the given productions. The elements pointers are preserved, but for the array lists
     * are new instances created.
     * @param rightHandSides    The production list
     * @return  A copy of the production list
     * @see RuleUtils#copyRightHandSide(ArrayList)
     */
    public static ArrayList<ArrayList<RHSElem>> copyRightHandSides(ArrayList<ArrayList<RHSElem>> rightHandSides) {
        ArrayList<ArrayList<RHSElem>> result = new ArrayList<>();
        for (ArrayList<RHSElem> righHandSide : rightHandSides) {
            result.add(copyRightHandSide(righHandSide));
        }
        return result;
    }

    /**
     * Helper function of {@link RuleUtils#copyRightHandSides(ArrayList)}. Creates a copy of the given production.
     * The elements pointers are preserved, but for the array list a new instance is created.
     * @param righHandSide  The production
     * @return  A copy of the production
     * @see RuleUtils#copyRHSElem(RHSElem)
     */
    private static ArrayList<RHSElem> copyRightHandSide(ArrayList<RHSElem> righHandSide) {
        ArrayList<RHSElem> result = new ArrayList<>();
        for (RHSElem elem : righHandSide) {
            result.add(copyRHSElem(elem));
        }
        return result;
    }

    /**
     * Helper function of {@link RuleUtils#copyRightHandSide(ArrayList)}. Creates a copy of the given RHSElem.
     *
     * If the element is not an instance of {@link ParExpr} or {@link NonTermExpr}, then the element itself is simply returned,
     * else a copy of the element is returned.
     * @param elem  The element
     * @return  A copy of the element
     */
    private static RHSElem copyRHSElem(RHSElem elem) {
        if (elem instanceof ParExpr)
            return new ParExpr(copyRightHandSide(((ParExpr) elem).getElems()));
        if (elem instanceof NonTermExpr) {
            NonTermExpr nonTermExpr = (NonTermExpr) elem;
            return new NonTermExpr(nonTermExpr.getModule(), nonTermExpr.getName());
        }
        return elem;
    }

    /**
     * Sets the module of all non-terminals (in the given productions) which have no module specified to the given module.
     * @param rightHandSides    A list of productions.
     * @param module            The name of the module to be set
     * @see RuleUtils#changeNormalNonTermsToImportedNonTermsRHS(ArrayList, String)
     */
    public static void changeNormalNonTermsToImportedNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides, String module) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeNormalNonTermsToImportedNonTermsRHS(rhs, module);
        }
    }

    /**
     * Helper function of {@link RuleUtils#changeNormalNonTermsToImportedNonTermsRHSs(ArrayList, String)}. Sets the
     * module of all non-terminals (in the given productions) which have no module specified to the given module.
     * @param rightHandSide     The production
     * @param module            The name of the module to be set
     * @see RuleUtils#changeNormalNonTermsToImportedNonTermsRHS(ArrayList, String)
     */
    private static void changeNormalNonTermsToImportedNonTermsRHS(ArrayList<RHSElem> rightHandSide, String module) {
        for (RHSElem rhsElem : rightHandSide) {
            if (rhsElem instanceof NonTermExpr) {
                NonTermExpr nonTermExpr = (NonTermExpr) rhsElem;
                if (nonTermExpr.getModule() == null)
                    nonTermExpr.setModule(module);
            } else if (rhsElem instanceof ParExpr) {
                changeNormalNonTermsToImportedNonTermsRHS(((ParExpr) rhsElem).getElems(), module);
            }
        }
    }

    /**
     * Sets the module of all non-terminals in the given productions to {@code null}.
     * @param rightHandSides    The productions
     * @see RuleUtils#changeAllImportedNonTermsToNormalNonTermsRHS(ArrayList)
     */
    public static void changeAllImportedNonTermsToNormalNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeAllImportedNonTermsToNormalNonTermsRHS(rhs);
        }
    }

    /**
     * Helper function of {@link RuleUtils#changeAllImportedNonTermsToNormalNonTermsRHSs(ArrayList)}Sets the module of
     * all non-terminals in the given production to {@code null}.
     * @param rightHandSide     The production
     */
    private static void changeAllImportedNonTermsToNormalNonTermsRHS(ArrayList<RHSElem> rightHandSide) {
        for (RHSElem rhsElem : rightHandSide) {
            if (rhsElem instanceof NonTermExpr) {
                NonTermExpr nonTermExpr = (NonTermExpr) rhsElem;
                nonTermExpr.setModule(null);
            } else if (rhsElem instanceof ParExpr) {
                changeAllImportedNonTermsToNormalNonTermsRHS(((ParExpr) rhsElem).getElems());
            }
        }
    }

    /**
     * Sets the module of the non-terminals that match the given module and non-terminal name to {@code null}.
     * @param rightHandSides    The productions.
     * @param module            The name of the module.
     * @param rule              The name of the non-terminal.
     */
    public static void changeSelectImportedNonTermsToNormalNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides, String module, String rule) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeSelectImportedNonTermsToNormalNonTermsRHS(rhs, module, rule);
        }
    }

    /**
     * Helper function of {@link RuleUtils#changeSelectImportedNonTermsToNormalNonTermsRHSs(ArrayList, String, String)}.
     * Sets the module of the non-terminals that match the given module and non-terminal name to {@code null}.
     * @param rightHandSide     The production.
     * @param module            The name of the module
     * @param rule              The name of the non-terminal
     */
    private static void changeSelectImportedNonTermsToNormalNonTermsRHS(ArrayList<RHSElem> rightHandSide, String module, String rule) {
        for (RHSElem rhsElem : rightHandSide) {
            if (rhsElem instanceof NonTermExpr) {
                NonTermExpr nonTermExpr = (NonTermExpr) rhsElem;
                if (nonTermExpr.getModule() != null && nonTermExpr.getModule().equals(module) && nonTermExpr.getName().equals(rule))
                    nonTermExpr.setModule(null);
            } else if (rhsElem instanceof ParExpr) {
                changeSelectImportedNonTermsToNormalNonTermsRHS(((ParExpr) rhsElem).getElems(), module, rule);
            }
        }
    }

    /**
     * Add the productions to the rules. A production is only added if it does not already exist in the productions of
     * the rule.
     * @param rule              The rule to which the productions are added.
     * @param rightHandSides    The productions that are added.
     */
    public static void addRHSsNoDuplicateProductions(Rule rule, ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides)
            if (!rule.getRightHandSides().contains(rhs))
                rule.getRightHandSides().add(rhs);
    }

    /**
     * Changes the name of the non-terminals to the given {@code localRule} if:
     * <p><ul>
     *      <li>The module name is {@code null}
     *      <li>The non-terminal name is {@code importRule}
     * </ul>
     * @param importRule        The name of the non-terminals that are changed.
     * @param localRule         The name that the non-terminal names are set to.
     * @param rightHandSides    The productions in which the non-terminals are changed
     */
    public static void replaceNontermRHSs(String importRule, String localRule, ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            replaceNontermRHS(importRule, localRule, rhs);
        }
    }


    /**
     * Helper function of {@link RuleUtils#replaceNontermRHSs(String, String, ArrayList)} Changes the name of the
     * non-terminals to the given {@code localRule} if:
     * <p><ul>
     *      <li>The module name is {@code null}
     *      <li>The non-terminal name is {@code importRule}
     * </ul>
     * @param importRule        The name of the non-terminals that are changed.
     * @param localRule         The name that the non-terminal names are set to.
     * @param rightHandSide     The production in which the non-terminals are changed
     */
    private static void replaceNontermRHS(String importRule, String localRule, ArrayList<RHSElem> rightHandSide) {
        for (RHSElem rhsElem : rightHandSide) {
            if (rhsElem instanceof NonTermExpr) {
                NonTermExpr nonTerm = (NonTermExpr) rhsElem;
                if (nonTerm.getModule() == null && nonTerm.getName().equals(importRule))
                    nonTerm.setName(localRule);
            } else if (rhsElem instanceof ParExpr) {
                replaceNontermRHS(importRule, localRule, ((ParExpr) rhsElem).getElems());
            }
        }
    }
}
