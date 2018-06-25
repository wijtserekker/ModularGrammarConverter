package nl.utwente.modgram.misc;

import nl.utwente.modgram.model.Rule;
import nl.utwente.modgram.model.rhs.NonTermExpr;
import nl.utwente.modgram.model.rhs.ParExpr;
import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;

public class RuleUtils {

    public static ArrayList<ArrayList<RHSElem>> copyRightHandSides(ArrayList<ArrayList<RHSElem>> rightHandSides) {
        ArrayList<ArrayList<RHSElem>> result = new ArrayList<>();
        for (ArrayList<RHSElem> righHandSide : rightHandSides) {
            result.add(copyRightHandSide(righHandSide));
        }
        return result;
    }

    private static ArrayList<RHSElem> copyRightHandSide(ArrayList<RHSElem> righHandSide) {
        ArrayList<RHSElem> result = new ArrayList<>();
        for (RHSElem elem : righHandSide) {
            result.add(copyRHSElem(elem));
        }
        return result;
    }

    private static RHSElem copyRHSElem(RHSElem elem) {
        if (elem instanceof ParExpr)
            return new ParExpr(copyRightHandSide(((ParExpr) elem).getElems()));
        if (elem instanceof NonTermExpr) {
            NonTermExpr nonTermExpr = (NonTermExpr) elem;
            return new NonTermExpr(nonTermExpr.getModule(), nonTermExpr.getName());
        }
        return elem;
    }

    public static void changeNormalNonTermsToImportedNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides, String module) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeNormalNonTermsToImportedNonTermsRHS(rhs, module);
        }
    }

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

    public static void changeAllImportedNonTermsToNormalNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeAllImportedNonTermsToNormalNonTermsRHS(rhs);
        }
    }

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

    public static void changeSelectImportedNonTermsToNormalNonTermsRHSs(ArrayList<ArrayList<RHSElem>> rightHandSides, String module, String rule) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            changeSelectImportedNonTermsToNormalNonTermsRHS(rhs, module, rule);
        }
    }

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

    public static void addRHSsNoDuplicateProductions(Rule rule, ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides)
            if (!rule.getRightHandSides().contains(rhs))
                rule.getRightHandSides().add(rhs);
    }

    public static void replaceNontermRHSs(String importRule, String localRule, ArrayList<ArrayList<RHSElem>> rightHandSides) {
        for (ArrayList<RHSElem> rhs : rightHandSides) {
            replaceNontermRHS(importRule, localRule, rhs);
        }
    }

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
